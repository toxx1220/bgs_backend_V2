package de.bgs.secondary.git

import de.bgs.PostgresqlContainerBaseTest
import de.bgs.secondary.GameFamilyJpaRepo
import de.bgs.secondary.database.BoardGameItem
import de.bgs.secondary.database.GameFamily
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource
import java.io.File

@TestPropertySource(
    properties = [
        "git.repo-root=src/test/resources",
        "git.board-game-csv-file-name=bgg_GameItem.csv",
        "git.game-family-csv-file-name=bgg_GameFamily.csv"
    ]
)
class CsvServiceTest : PostgresqlContainerBaseTest() {

    @Autowired
    private lateinit var gameFamilyRepo: GameFamilyJpaRepo

    @Autowired
    private lateinit var csvService: CsvService

    private val repoRoot: File = File("src/test/resources")

    @BeforeEach
    fun setUp() {
        gameFamilyRepo.deleteAll()
    }

    @Test
    fun parseGameFamily() {
        val gameFamilies = csvService.parseGameFamily(repoRoot)
        assertThat(gameFamilies).containsExactly(
            GameFamily(null, 66553, "Components: Control Boards"),
            GameFamily(null, 64990, "Components: Meeples (Animal) / Animeeples"),
            GameFamily(null, 68769, "Components: Wooden pieces & boards")
        )
    }

    @Test
    fun parseGameItem() {
        val gameFamilies = listOf(
            GameFamily(null, 66553, "Components: Control Boards"),
            GameFamily(null, 64990, "Components: Meeples (Animal) / Animeeples"),
            GameFamily(null, 68769, "Components: Wooden pieces & boards")
        )
        val expectedGameFamilies = gameFamilyRepo.saveAll(gameFamilies).toMutableSet()

        val boardGames = csvService.parseBoardGame(repoRoot)

        assertThat(boardGames).containsExactly(
            BoardGameItem(
                null,
                183394,
                "Viticulture Essential Edition",
                year = 2015,
                minPlayers = 1,
                maxPlayers = 6,
                minPlayersRec = 1,
                maxPlayersRec = 5,
                minPlayersBest = 3,
                maxPlayersBest = 4,
                minAge = 13,
                minAgeRec = 11.88,
                minTime = 45,
                maxTime = 90,
                cooperative = false,
                gameFamilies = expectedGameFamilies,
                rank = 39,
                numVotes = 50886,
                avgRating = 7.97994,
                stdDevRating = 1.27151,
                bayesRating = 7.82833,
                complexity = 2.8926,
                languageDependency = 3.1323529411764706,
            )
        )
    }
}
