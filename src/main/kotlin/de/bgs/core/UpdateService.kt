package de.bgs.core

import de.bgs.secondary.GameFamilyJpaRepo
import de.bgs.secondary.database.BoardGameItem
import de.bgs.secondary.database.GameFamily
import de.bgs.secondary.git.CsvService
import de.bgs.secondary.git.GitService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Path
import java.util.concurrent.TimeUnit
import kotlin.io.path.Path
import kotlin.system.exitProcess

@Service
class UpdateService(
    private val gitService: GitService,
    private val csvService: CsvService,
    private val boardGameService: BoardGameService,
    private val gameFamilyJpaRepo: GameFamilyJpaRepo
) {
    companion object {
        const val REPO_URL_FORMAT_STRING: String = "https://oauth2:{0}@framagit.org/r.g/board-game-data"
        val PROJECT_ROOT: Path = Path(System.getProperty("user.dir"))
        val DATA_DIRECTORY: File = File(PROJECT_ROOT.toFile(), "data")
    }

    private val log = KotlinLogging.logger {}

    @Scheduled(timeUnit = TimeUnit.DAYS, fixedRate = 7)
    fun updateDatabase(): String {
        // get git repo
        val repo = gitService.cloneGitRepository(DATA_DIRECTORY)
        if (repo.isEmpty) {
            log.error { "Cloning git Repo failed!" }
            exitProcess(69)
        }
        log.info { "Cloning git Repo successful!" }
        // parse CSVs and update database
        val parsedItems: List<BoardGameItem> = parseCsv()
        boardGameService.saveBoardGames(parsedItems)
        return "TODO: Implement me!"
    }

    fun parseCsv(): List<BoardGameItem> {
        val gameFamilies: List<GameFamily> = csvService.parseGameFamily()
        gameFamilyJpaRepo.saveAllAndFlush(gameFamilies)
        return csvService.parseBoardGame() // bggId, Item
    }
}
