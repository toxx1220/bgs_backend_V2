package de.bgs.core

import de.bgs.secondary.database.BoardGameItem
import de.bgs.secondary.database.GameFamily
import de.bgs.secondary.git.CsvService
import de.bgs.secondary.git.GitService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import kotlin.io.path.absolutePathString
import kotlin.system.exitProcess

@Service
class UpdateService(
    private val gitService: GitService,
    private val csvService: CsvService,
    private val boardGameService: BoardGameService
) {
    companion object {
        const val REPO_URL_FORMAT_STRING: String = "https://oauth2:{0}@framagit.org/r.g/board-game-data"
        private val REPO_ROOT = Paths.get("")
    }

    private val log = KotlinLogging.logger {}

    @Scheduled(timeUnit = TimeUnit.DAYS, fixedRate = 7)
    fun updateDatabase(): String {
        // get git repo
        val repo = gitService.cloneGitRepository(File(REPO_ROOT.absolutePathString()))
        if (repo.isEmpty) {
            log.error { "Cloning git Repo failed!" }
            exitProcess(69)
        }
        // parse CSVs and update database
        val parsedItems: List<BoardGameItem> = parseCsv()
        boardGameService.saveBoardGames(parsedItems)
        return "TODO: Implement me!"
    }

    fun parseCsv(): List<BoardGameItem> {
        val gameFamilies: List<GameFamily> = csvService.parseGameFamily()
        val bggIdBoardGameMap: HashMap<Long, BoardGameItem> = csvService.parseBoardGame() // bggId, Item
        gameFamilies.forEach {
            val boardGame = bggIdBoardGameMap.get(it.g)
        }
    }
}
