package de.bgs.secondary.git

import de.bgs.MockGitServiceConfig
import de.bgs.PostgresqlContainerBaseTest
import de.bgs.secondary.database.GameFamily
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
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
    private lateinit var csvService: CsvService

    @Test
    fun parseGameFamily() {
        val gameFamilies = csvService.parseGameFamily()
        assertAll(
            { assert(gameFamilies.size == 3) },
            { assert(gameFamilies[0] == GameFamily(null, 66553, "Components: Control Boards")) },
            { assert(gameFamilies[1] == GameFamily(null, 64990, "Components: Meeples (Animal) / Animeeples")) },
            { assert(gameFamilies[2] == GameFamily(null, 68769, "Components: Wooden pieces & boards")) },
        )
    }


}