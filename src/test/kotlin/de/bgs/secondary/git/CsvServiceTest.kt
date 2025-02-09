package de.bgs.secondary.git

import de.bgs.secondary.database.GameFamily
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

@SpringBootTest
@ExtendWith(MockitoExtension::class)
class CsvServiceTest {

    val csvService: CsvService = Mockito.mock(CsvService::class.java)

    @Test
    fun parseGameFamily() {
        Mockito.`when`(csvService.getFileReader(any()))
            .thenReturn(File("src/test/resources/game_family.csv").reader())
        val gameFamilies = csvService.parseGameFamily()
        assertAll(
            { assert(gameFamilies.size == 2) },
            { assert(gameFamilies[0] == GameFamily(null, 66553, "Components: Control Boards")) },
            { assert(gameFamilies[1] == GameFamily(null, 64990, "Components: Meeples (Animal) / Animeeples")) },
            { assert(gameFamilies[2] == GameFamily(null, 68769, "Components: Wooden pieces & boards")) },
        )
    }

    @Test
    fun parseBoardGame() {
    }

}