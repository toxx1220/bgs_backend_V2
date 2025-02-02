package de.bgs.core

import de.bgs.secondary.database.BoardGameItem
import de.bgs.secondary.database.GameFamily
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import kotlin.io.path.absolutePathString

@Service
class UpdateService(private val gitService: GitService, private val boardGameService: BoardGameService) {
    companion object {
        const val REPO_URL_FORMAT_STRING: String = "https://oauth2:{0}@framagit.org/r.g/board-game-data"
        private val REPO_ROOT = Paths.get("")
    }
    private val log = KotlinLogging.logger() {}

    @Scheduled(timeUnit = TimeUnit.DAYS, fixedRate = 7)
    fun updateDatabase(): String {
        // get git repo
        val repo = gitService.cloneGitRepository(File(REPO_ROOT.absolutePathString()))
        repo.isEmpty.let {
            log.error { "Cloning git Repo failed!" }
        }
        // parse CSVs and update database
        val parsedItems: List<BoardGameItem> = parseCsv()
        boardGameService.saveBoardGames(parsedItems)
        return "TODO: Implement me!"
    }

    fun parseCsv(): List<BoardGameItem> {
        val bgItem = BoardGameItem(
            bggId = 0,
            name = "test-parse",
            year = 0,
            minPlayers = 0,
            maxPlayers = 0,
            minPlayersRec = 0,
            maxPlayersRec = 0,
            minPlayersBest = 0,
            maxPlayersBest = 0,
            minAge = 0,
            minAgeRec = 0,
            minTime = 0,
            maxTime = 0,
            cooperative = false,
            compilation = false,
            compilationOf = "",
            implementation = "",
            integration = "",
            rank = 0,
            numVotes = 0,
            avgRating = 0.0,
            stdDevRating = 0.0,
            bayesRating = 0.0,
            complexity = 0.0,
            languageDependency = 0.0
        )
        bgItem.setGameFamily(
            mutableSetOf(
                GameFamily(
                    gameFamilyId = 0,
                    name = "test-family",
                    boardGame = mutableSetOf(bgItem)
                )
            )
        )
        return listOf(bgItem)
    }
}
