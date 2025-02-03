package de.bgs.secondary.git

import de.bgs.core.UpdateService.Companion.PROJECT_ROOT
import de.bgs.secondary.GameFamilyJpaRepo
import de.bgs.secondary.database.BoardGameItem
import de.bgs.secondary.database.GameFamily
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.commons.csv.CSVFormat
import org.springframework.stereotype.Service
import kotlin.io.path.reader

@Service
class CsvService(private val gameFamilyJpaRepo: GameFamilyJpaRepo) {
    companion object {
        private const val CSV_FOLDER = "scraped"
        const val GAME_FAMILY_CSV_PATH = CSV_FOLDER + "bgg_GameFamily.csv"
        const val BOARD_GAME_ITEM_CSV_PATH = CSV_FOLDER + "bgg_GameItem.csv"
        val logger = KotlinLogging.logger {}
    }

    fun parseGameFamily(): List<GameFamily> {
        return CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
            setIgnoreSurroundingSpaces(true)
        }.get().parse(PROJECT_ROOT.resolve(GAME_FAMILY_CSV_PATH).reader())
            .drop(1) // Dropping the header
            .map {
                GameFamily(
                    gameFamilyId = it[0].toLong(),
                    name = it[1]
                )
            }
    }

    fun parseBoardGame(): List<BoardGameItem> { //
        return CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
            setIgnoreSurroundingSpaces(true)
        }.get().parse(PROJECT_ROOT.resolve(BOARD_GAME_ITEM_CSV_PATH).reader())
            .drop(1) // Dropping the header
            .map {
                val boardGameItem = BoardGameItem(
                    bggId = it[0].toLong(),
                    name = it[1],
                    year = it[2].toInt(),
                    // game type [3]
                    // designer [4]
                    // artist [5]
                    // publisher [6]
                    minPlayers = it[7].toInt(),
                    maxPlayers = it[8].toInt(),
                    minPlayersRec = it[9].toInt(),
                    maxPlayersRec = it[10].toInt(),
                    minPlayersBest = it[11].toInt(),
                    maxPlayersBest = it[12].toInt(),
                    minAge = it[13].toInt(),
                    minAgeRec = it[14].toInt(),
                    minTime = it[15].toInt(),
                    maxTime = it[16].toInt(),
                    // category [17]
                    // mechanic [18]
                    cooperative = it[19].toBoolean(),
                    compilation = it[20].toBoolean(),
//                    compilationOf = it[21],
                    family = getLinkedFamilies(it[22]),
//                    implementation = it[23],
//                    integration = it[24],
                    rank = it[25].toInt(),
                    numVotes = it[26].toInt(),
                    avgRating = it[27].toDouble(),
                    stdDevRating = it[28].toDouble(),
                    bayesRating = it[29].toDouble(),
                    complexity = it[30].toDouble(),
                    languageDependency = it[31].toDouble()
                )
                logger.info { "Successfully parsed $boardGameItem" }
                return@map boardGameItem
            }
    }

    private fun getLinkedFamilies(familyIds: String): MutableSet<GameFamily> {
        if (familyIds.isEmpty()) return mutableSetOf()

        val familyIdList: List<Long> = familyIds.split(",").map { it.toLong() }
        return gameFamilyJpaRepo.findByGameFamilyIdIn(familyIdList)
    }
}