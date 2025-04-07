package de.bgs.primary

import de.bgs.PostgresqlContainerBaseTest
import de.bgs.core.BoardGameFilterField
import de.bgs.core.FilterCondition
import de.bgs.core.FilterRequest
import de.bgs.core.Operator
import de.bgs.secondary.BoardGameJpaRepo
import de.bgs.secondary.database.BoardGameItem
import de.bgs.util.BoardGameTestData.Companion.testBoardGame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import java.util.stream.Stream
import kotlin.test.assertEquals

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class BoardGameControllerTest : PostgresqlContainerBaseTest() {

    @Autowired
    private lateinit var boardGameJpaRepo: BoardGameJpaRepo

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    companion object {
        @JvmStatic
        fun getPositiveAndNegativeTestItems(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    1, // filter value
                    testBoardGame().apply { bggId = 1 }, // positive
                    testBoardGame().apply { bggId = 2 }, // negative
                    "BGG_ID", // filter field
                    Operator.EQUALS
                ),
                Arguments.of(
                    "name",
                    testBoardGame().apply { name = "name" },
                    testBoardGame().apply { name = "other name" },
                    "NAME",
                    Operator.EQUALS
                ),
                Arguments.of(
                    "description",
                    testBoardGame().apply { description = "description" },
                    testBoardGame().apply { description = "other description" },
                    "DESCRIPTION",
                    Operator.EQUALS
                ),
                Arguments.of(
                    1,
                    testBoardGame().apply { minPlayers = 1 },
                    testBoardGame().apply { minPlayers = 2 },
                    "MIN_PLAYERS",
                    Operator.EQUALS
                ),
                Arguments.of(
                    2,
                    testBoardGame().apply { maxPlayers = 2 },
                    testBoardGame().apply { maxPlayers = 3 },
                    "MAX_PLAYERS",
                    Operator.EQUALS
                ),
                Arguments.of(
                    1,
                    testBoardGame().apply { minPlayersRec = 1 },
                    testBoardGame().apply { minPlayersRec = 2 },
                    "MIN_PLAYERS_REC",
                    Operator.EQUALS
                ),
                Arguments.of(
                    2,
                    testBoardGame().apply { maxPlayersRec = 2 },
                    testBoardGame().apply { maxPlayersRec = 3 },
                    "MAX_PLAYERS_REC",
                    Operator.EQUALS
                ),
                Arguments.of(
                    1,
                    testBoardGame().apply { minPlayersBest = 1 },
                    testBoardGame().apply { minPlayersBest = 2 },
                    "MIN_PLAYERS_BEST",
                    Operator.EQUALS
                ),
                Arguments.of(
                    2,
                    testBoardGame().apply { maxPlayersBest = 2 },
                    testBoardGame().apply { maxPlayersBest = 3 },
                    "MAX_PLAYERS_BEST",
                    Operator.EQUALS
                ),
                Arguments.of(
                    1,
                    testBoardGame().apply { minAge = 1 },
                    testBoardGame().apply { minAge = 2 },
                    "MIN_AGE",
                    Operator.EQUALS
                ),
                Arguments.of(
                    1.0,
                    testBoardGame().apply { minAgeRec = 1.0 },
                    testBoardGame().apply { minAgeRec = 2.0 },
                    "MIN_AGE_REC",
                    Operator.EQUALS
                ),
                Arguments.of(
                    1,
                    testBoardGame().apply { minTime = 1 },
                    testBoardGame().apply { minTime = 2 },
                    "MIN_TIME",
                    Operator.EQUALS
                ),
                Arguments.of(
                    2,
                    testBoardGame().apply { maxTime = 2 },
                    testBoardGame().apply { maxTime = 3 },
                    "MAX_TIME",
                    Operator.EQUALS
                ),
                Arguments.of(
                    1,
                    testBoardGame().apply { rank = 1 },
                    testBoardGame().apply { rank = 2 },
                    "RANK",
                    Operator.EQUALS
                ),
                Arguments.of(
                    1,
                    testBoardGame().apply { numVotes = 1 },
                    testBoardGame().apply { numVotes = 2 },
                    "NUM_VOTES",
                    Operator.EQUALS
                ),
                Arguments.of(
                    1.0,
                    testBoardGame().apply { avgRating = 1.0 },
                    testBoardGame().apply { avgRating = 2.0 },
                    "AVG_RATING",
                    Operator.EQUALS
                ),
                Arguments.of(
                    1.0,
                    testBoardGame().apply { stdDevRating = 1.0 },
                    testBoardGame().apply { stdDevRating = 2.0 },
                    "STD_DEV_RATING",
                    Operator.EQUALS
                ),
                Arguments.of(
                    1.0,
                    testBoardGame().apply { bayesRating = 1.0 },
                    testBoardGame().apply { bayesRating = 2.0 },
                    "BAYES_RATING",
                    Operator.EQUALS
                ),
                Arguments.of(
                    1.0,
                    testBoardGame().apply { complexity = 1.0 },
                    testBoardGame().apply { complexity = 2.0 },
                    "COMPLEXITY",
                    Operator.EQUALS
                ),
                Arguments.of(
                    1.0,
                    testBoardGame().apply { languageDependency = 1.0 },
                    testBoardGame().apply { languageDependency = 2.0 },
                    "LANGUAGE_DEPENDENCY",
                    Operator.EQUALS
                ),
            )
        }
    }

    @BeforeEach
    fun setUp() {
        boardGameJpaRepo.deleteAll()
    }

    @ParameterizedTest
    @MethodSource("getPositiveAndNegativeTestItems")
    fun `Should return the positive BoardGameItem`(
        filterValue: Any,
        positiveBoardGame: BoardGameItem,
        negativeBoardGame: BoardGameItem,
        filterField: String,
        operator: Operator
    ) {
        // given
        assertAll(
            { assertEquals(positiveBoardGame.bggId, boardGameJpaRepo.save(positiveBoardGame).bggId) },
            { assertEquals(negativeBoardGame.bggId, boardGameJpaRepo.save(negativeBoardGame).bggId) }
        )
        val filterRequest =
            FilterRequest(setOf(FilterCondition(BoardGameFilterField.valueOf(filterField), operator, filterValue)))

        // when
        val result = restTemplate.postForEntity("/boardgame", filterRequest, BoardGamePageDto::class.java)

        // then
        assertAll(
            { assertEquals(HttpStatus.OK, result.statusCode) },
            { assertEquals(listOf(positiveBoardGame), result.body?.boardGameItems) }
        )
    }

}