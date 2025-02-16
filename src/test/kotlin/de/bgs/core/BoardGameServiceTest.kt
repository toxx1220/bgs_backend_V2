package de.bgs.core

import de.bgs.MockGitServiceConfig
import de.bgs.PostgresqlContainerBaseTest
import de.bgs.secondary.GameFamilyJpaRepo
import de.bgs.secondary.database.BoardGameItem
import de.bgs.secondary.database.GameFamily
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import

@Import(MockGitServiceConfig::class)
class BoardGameServiceTest : PostgresqlContainerBaseTest() {

    @Autowired
    private lateinit var boardGameService: BoardGameService

    @Autowired
    private lateinit var gameFamilyJpaRepo: GameFamilyJpaRepo

    @Test
    fun saveBoardGame() {
        val gameFamily = GameFamily(
            gameFamilyId = 1,
            name = "Test Family"
        )
        val savedGameFamily = gameFamilyJpaRepo.save(gameFamily)
        val boardGameItem = BoardGameItem(
            bggId = 1,
            name = "Test Game",
            minPlayers = 1,
            maxPlayers = 4,
            year = 2020
        )
        val managedGameFamily = gameFamilyJpaRepo.findByGameFamilyId(savedGameFamily.gameFamilyId).get()
        boardGameItem.setGameFamily(mutableSetOf(managedGameFamily))

        val savedBoardGame = boardGameService.saveBoardGame(boardGameItem)

        assertAll(
            { Assertions.assertThat(savedBoardGame.bggId).isEqualTo(boardGameItem.bggId) },
            { Assertions.assertThat(savedBoardGame.name).isEqualTo(boardGameItem.name) },
            { Assertions.assertThat(savedBoardGame.minPlayers).isEqualTo(boardGameItem.minPlayers) },
            { Assertions.assertThat(savedBoardGame.maxPlayers).isEqualTo(boardGameItem.maxPlayers) },
            { Assertions.assertThat(savedBoardGame.year).isEqualTo(boardGameItem.year) },
            { Assertions.assertThat(savedBoardGame.gameFamilies).containsExactly(savedGameFamily) }
        )
    }

}