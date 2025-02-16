package de.bgs.core

import de.bgs.secondary.GameFamilyJpaRepo
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
    private val boardGameService: BoardGameService,
    private val gameFamilyJpaRepo: GameFamilyJpaRepo,
    gitConfigurationProperties: GitConfigurationProperties
) {
    private val logger = KotlinLogging.logger {}
    private val schedulerEnabled = gitConfigurationProperties.schedulerEnabled

    @Scheduled(timeUnit = DAYS, fixedRate = 7)
    fun updateDatabase() {
        if (!schedulerEnabled) {
            logger.info { "Scheduler is disabled" }
            return
        }
        
        // update/pull git repo
        val repo: Repository = getRepository()
        gitService.pull(repo)
        // parse CSVs and update database
        val parsedItems: List<BoardGameItem> = parseCsv(repo)
        boardGameService.saveBoardGames(parsedItems)
        logger.info { "Successfully saved ${parsedItems.size} BoardGameItems" }
    }

    fun parseCsv(repo: Repository): List<BoardGameItem> {
        val gameFamilies: List<GameFamily> = csvService.parseGameFamily(repo.workTree)
        val existingGameFamilies: Map<Long, GameFamily> = gameFamilyJpaRepo.findAll().associateBy { it.gameFamilyId }

        val mergedFamilies = gameFamilies.map { newFamily ->
            existingGameFamilies[newFamily.gameFamilyId]?.also { existing ->
                existing.name = newFamily.name
            } ?: newFamily
        }

        gameFamilyJpaRepo.saveAll(mergedFamilies)
        logger.info { "Successfully saved ${mergedFamilies.size} GameFamilies" }
        return csvService.parseBoardGame(repo.workTree)
    }

    fun getRepository(): Repository {
        return gitService.getRepository().orElseGet {
            gitService.cloneGitRepository().orElseThrow { Exception("Could not get or clone repository") }
        }
    }
}
