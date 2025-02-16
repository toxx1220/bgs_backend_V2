package de.bgs.core

import de.bgs.secondary.*
import de.bgs.secondary.database.BoardGameItem
import de.bgs.secondary.database.GameFamily
import de.bgs.secondary.git.CsvService
import de.bgs.secondary.git.GitConfigurationProperties
import de.bgs.secondary.git.GitService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.eclipse.jgit.lib.Repository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit.DAYS

@Component
class UpdateTask(
    private val gitService: GitService,
    private val csvService: CsvService,
    gitConfigurationProperties: GitConfigurationProperties,
    private val boardGameService: BoardGameService,
    private val gameFamilyJpaRepo: GameFamilyJpaRepo,
    private val gameTypeJpaRepo: GameTypeJpaRepo,
    private val personJpaRepo: PersonJpaRepo,
    private val categoryJpaRepo: CategoryJpaRepo,
    private val mechanicJpaRepo: MechanicJpaRepo,
    private val publisherJpaRepo: PublisherJpaRepo
) {
    private val logger = KotlinLogging.logger {}
    private val schedulerEnabled = gitConfigurationProperties.schedulerEnabled

    @Scheduled(timeUnit = DAYS, fixedRate = 7)
    fun updateDatabase() {
        if (!schedulerEnabled) {
            logger.info { "Scheduler is disabled" }
            return
        }
        boardGameService.deleteDatabase()
        // update/pull git repo
        val repo: Repository = getRepository()
        gitService.pull(repo)
        // parse CSVs and update database
        val parsedItems: List<BoardGameItem> = parseCsv(repo)
        boardGameService.saveBoardGames(parsedItems)
        logger.info { "Successfully saved ${parsedItems.size} BoardGameItems" }
    }

    fun parseCsv(repo: Repository): List<BoardGameItem> {
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
        val publisherMap = publisherJpaRepo.saveAll(csvService.parsePublisher(repo.workTree)).associateBy { it.bggId }
        logger.info { "Successfully saved ${publisherMap.size} Publisher" }

        return csvService.parseBoardGame(
            repo.workTree,
            gameFamilyMap,
            gameTypeMap,
            personMap,
            categoryMap,
            mechanicMap,
            publisherMap
        )
    }

    fun getRepository(): Repository {
        return gitService.getRepository().orElseGet {
            gitService.cloneGitRepository().orElseThrow { Exception("Could not get or clone repository") }
        }
    }
}
