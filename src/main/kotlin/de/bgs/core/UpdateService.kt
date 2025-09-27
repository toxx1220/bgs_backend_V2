package de.bgs.core

import de.bgs.secondary.*
import de.bgs.secondary.database.GameFamily
import de.bgs.secondary.database.UpdateTaskInformation
import de.bgs.secondary.git.CsvService
import de.bgs.secondary.git.GitConfigurationProperties
import de.bgs.secondary.git.GitService
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.stream.consumeAsFlow
import kotlinx.coroutines.sync.Mutex
import org.eclipse.jgit.lib.Repository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit.DAYS

@Component
class UpdateService(
    private val gitService: GitService,
    private val csvService: CsvService,
    gitConfigurationProperties: GitConfigurationProperties,
    private val boardGameService: BoardGameService,
    private val gameFamilyJpaRepo: GameFamilyJpaRepo,
    private val gameTypeJpaRepo: GameTypeJpaRepo,
    private val personJpaRepo: PersonJpaRepo,
    private val categoryJpaRepo: CategoryJpaRepo,
    private val mechanicJpaRepo: MechanicJpaRepo,
    private val publisherJpaRepo: PublisherJpaRepo,
    private val boardGameJpaRepo: BoardGameJpaRepo,
    private val updateTaskInformationRepo: UpdateTaskInformationRepo
) {
    private val logger = KotlinLogging.logger {}
    private val schedulerEnabled = gitConfigurationProperties.schedulerEnabled
    private var skipFirstExecution = gitConfigurationProperties.skipFirstExecution
    private val utcZone = ZoneOffset.UTC
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val mutex = Mutex()

    @Scheduled(timeUnit = DAYS, fixedRate = 7)
    private suspend fun scheduledUpdateDatabase() {
        if (!schedulerEnabled || skipFirstExecution) {
            logger.info { "Scheduler is enabled: $schedulerEnabled, skip execution: $skipFirstExecution" }
            skipFirstExecution = false
            return
        }
        updateDatabase()
    }

    fun triggerUpdateDatabase() {
        serviceScope.launch {
            updateDatabase()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun updateDatabase() = withContext(Dispatchers.IO) {
        if (!mutex.tryLock()) {
            logger.info { "Update task is locked, skipping execution." }
            return@withContext
        }
        try {
            val updateStartTime: LocalDateTime = LocalDateTime.now(utcZone)
            val lastExecution = updateTaskInformationRepo.findTopByOrderByLastUpdateTaskTimeDesc()?.lastUpdateTaskTime
                ?: LocalDateTime.MIN
            val atLeastOneDaySinceLastExecution = lastExecution.plusDays(1)
                .isBefore(updateStartTime)
            if (!atLeastOneDaySinceLastExecution) {
                logger.info { "Skipping execution of update task. Last execution was less than 24h ago." }
                return@withContext
            }

            boardGameService.clearDatabase()
            logger.info { "Database has been cleared!" }

            // update/pull git repo
            val repo: Repository = getRepository()
            gitService.pull(repo)

            // parse CSVs and update database
            val gameFamilyMap: Map<Long, GameFamily> =
                gameFamilyJpaRepo.saveAll(csvService.parseGameFamily(repo.workTree)).associateBy { it.bggId }
            logger.info { "Successfully saved ${gameFamilyMap.size} GameFamilies" }
            val gameTypeMap = gameTypeJpaRepo.saveAll(csvService.parseGameType(repo.workTree)).associateBy { it.bggId }
            logger.info { "Successfully saved ${gameTypeMap.size} GameTypes" }
            val personMap = personJpaRepo.saveAll(csvService.parsePerson(repo.workTree)).associateBy { it.bggId }
            logger.info { "Successfully saved ${personMap.size} Persons" }
            val categoryMap = categoryJpaRepo.saveAll(csvService.parseCategory(repo.workTree)).associateBy { it.bggId }
            logger.info { "Successfully saved ${categoryMap.size} Categories" }
            val mechanicMap = mechanicJpaRepo.saveAll(csvService.parseMechanic(repo.workTree)).associateBy { it.bggId }
            logger.info { "Successfully saved ${mechanicMap.size} Mechanics" }
            val publisherMap =
                publisherJpaRepo.saveAll(csvService.parsePublisher(repo.workTree)).associateBy { it.bggId }
            logger.info { "Successfully saved ${publisherMap.size} Publisher" }

            var totalCounter = 0
            csvService.parseBoardGamesStream(
                repo.workTree,
                gameFamilyMap,
                gameTypeMap,
                personMap,
                categoryMap,
                mechanicMap,
                publisherMap
            ).consumeAsFlow()
                .buffer(capacity = 16, onBufferOverflow = BufferOverflow.SUSPEND)
                .flowOn(Dispatchers.IO)
                .chunked(500)
                .onEach { items ->
                    boardGameJpaRepo.saveAll(items)
                    totalCounter += items.size
                    logger.info { "Executed Batch of size ${items.size}. # items saved: $totalCounter" }
                    System.gc()
                }
                .catch { e -> logger.error(e) { "Pipeline failed $e" } }
                .collect()
            val duration = Duration.between(updateStartTime, LocalDateTime.now(utcZone))
            val info = UpdateTaskInformation(
                lastUpdateTaskTime = updateStartTime,
                executionDurationInMinutes = duration.toMinutes()
            )
            updateTaskInformationRepo.save<UpdateTaskInformation>(info)
            logger.info { "finished update-job successfully in ${duration.toHoursPart()}H:${duration.toMinutesPart()}M:${duration.toSecondsPart()}S!" }
        } finally {
            mutex.unlock()
        }
    }

    fun getRepository(): Repository {
        return gitService.getRepository().orElseGet {
            gitService.cloneGitRepository().orElseThrow { Exception("Could not get or clone repository") }
        }
    }
}
