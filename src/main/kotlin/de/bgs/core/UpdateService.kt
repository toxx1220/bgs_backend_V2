package de.bgs.core

import de.bgs.secondary.GameFamilyJpaRepo
import de.bgs.secondary.database.BoardGameItem
import de.bgs.secondary.database.GameFamily
import de.bgs.secondary.git.CsvService
import de.bgs.secondary.git.GitService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@ConditionalOnProperty(value = ["git.gitSchedulerEnabled"], havingValue = "true")
@Service
class UpdateService(
    private val gitService: GitService,
    private val csvService: CsvService,
    private val boardGameService: BoardGameService,
    private val gameFamilyJpaRepo: GameFamilyJpaRepo
) {
    private val log = KotlinLogging.logger {}

    @Scheduled(timeUnit = TimeUnit.DAYS, fixedRate = 7)
    fun updateDatabase() {
        // update/pull git repo
        gitService.pull()
        // parse CSVs and update database
        val parsedItems: List<BoardGameItem> = parseCsv()
        boardGameService.saveBoardGames(parsedItems)
    }

    fun parseCsv(): List<BoardGameItem> {
        val gameFamilies: List<GameFamily> = csvService.parseGameFamily()
        gameFamilyJpaRepo.saveAllAndFlush(gameFamilies)
        return csvService.parseBoardGame() // bggId, Item
    }
}
