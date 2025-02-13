package de.bgs.secondary.git

import de.bgs.MockGitServiceConfig
import de.bgs.PostgresqlContainerBaseTest
import de.bgs.secondary.GameFamilyJpaRepo
import de.bgs.secondary.database.BoardGameItem
import de.bgs.secondary.database.GameFamily
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource

@TestPropertySource(
    properties = [
        "git.repo-folder=src/test/resources",
        "git.board-game-csv-file-name=bgg_GameItem.csv",
        "git.game-family-csv-file-name=bgg_GameFamily.csv"
    ]
)
@Import(MockGitServiceConfig::class)
class CsvServiceTest : PostgresqlContainerBaseTest() {

    @Autowired
    private lateinit var gameFamilyRepo: GameFamilyJpaRepo

    @Autowired
    private lateinit var csvService: CsvService

    @BeforeEach
    fun setUp() {
        gameFamilyRepo.deleteAll()
    }

    @Test
    fun parseGameFamily() {
        val gameFamilies = csvService.parseGameFamily()
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

        val boardGames = csvService.parseBoardGame()

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
                compilation = 1,
                family = expectedGameFamilies,
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
