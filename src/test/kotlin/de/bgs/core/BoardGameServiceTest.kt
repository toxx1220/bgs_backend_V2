package de.bgs.core

import de.bgs.PostgresqlContainerBaseTest
import de.bgs.secondary.*
import de.bgs.secondary.database.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired

class BoardGameServiceTest : PostgresqlContainerBaseTest() {

    @Autowired
    private lateinit var boardGameService: BoardGameService

    @Autowired
    private lateinit var gameFamilyJpaRepo: GameFamilyJpaRepo

    @Autowired
    private lateinit var gameTypeJpaRepo: GameTypeJpaRepo

    @Autowired
    private lateinit var personJpaRepo: PersonJpaRepo

    @Autowired
    private lateinit var categoryJpaRepo: CategoryJpaRepo

    @Autowired
    private lateinit var mechanicJpaRepo: MechanicJpaRepo

    @Autowired
    private lateinit var publisherJpaRepo: PublisherJpaRepo

    @Test
    fun saveBoardGame() {
        val gameFamily = GameFamily(
            bggId = 1,
            name = "Test Family"
        )
        val gameType = GameType(
            bggId = 1,
            name = "Test Type"
        )
        val person = Person(
            bggId = 1,
            name = "Test Person"
        )
        val gameCategory = GameCategory(
            bggId = 1,
            name = "Test Category"
        )
        val mechanic = Mechanic(
            bggId = 1,
            name = "Test Mechanic"
        )
        val publisher = Publisher(
            bggId = 1,
            name = "Test Publisher"
        )
        val savedGameFamily = gameFamilyJpaRepo.save(gameFamily)
        val savedGameType = gameTypeJpaRepo.save(gameType)
        val savedPerson = personJpaRepo.save(person)
        val savedCategory = categoryJpaRepo.save(gameCategory)
        val savedMechanic = mechanicJpaRepo.save(mechanic)
        val savedPublisher = publisherJpaRepo.save(publisher)

        val boardGameItem = BoardGameItem(
            bggId = 1,
            name = "Test Game",
            minPlayers = 1,
            maxPlayers = 4,
            year = 2020,
            gameFamilies = mutableSetOf(savedGameFamily),
            gameTypes = mutableSetOf(savedGameType),
            designer = mutableSetOf(savedPerson),
            artist = mutableSetOf(savedPerson),
            category = mutableSetOf(savedCategory),
            mechanic = mutableSetOf(savedMechanic),
            publisher = mutableSetOf(savedPublisher)
        )

        val savedBoardGame = boardGameService.saveBoardGame(boardGameItem)

        assertAll(
            { Assertions.assertThat(savedBoardGame.bggId).isEqualTo(boardGameItem.bggId) },
            { Assertions.assertThat(savedBoardGame.name).isEqualTo(boardGameItem.name) },
            { Assertions.assertThat(savedBoardGame.minPlayers).isEqualTo(boardGameItem.minPlayers) },
            { Assertions.assertThat(savedBoardGame.maxPlayers).isEqualTo(boardGameItem.maxPlayers) },
            { Assertions.assertThat(savedBoardGame.year).isEqualTo(boardGameItem.year) },
            { Assertions.assertThat(savedBoardGame.gameFamilies).containsExactly(savedGameFamily) },
            { Assertions.assertThat(savedBoardGame.gameTypes).containsExactly(savedGameType) },
            { Assertions.assertThat(savedBoardGame.designer).containsExactly(savedPerson) },
            { Assertions.assertThat(savedBoardGame.artist).containsExactly(savedPerson) },
            { Assertions.assertThat(savedBoardGame.category).containsExactly(savedCategory) },
            { Assertions.assertThat(savedBoardGame.mechanic).containsExactly(savedMechanic) },
            { Assertions.assertThat(savedBoardGame.publisher).containsExactly(savedPublisher) }
        )
    }

}