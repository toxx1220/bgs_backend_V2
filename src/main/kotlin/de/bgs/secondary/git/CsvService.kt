package de.bgs.secondary.git

import de.bgs.secondary.GameFamilyJpaRepo
import de.bgs.secondary.database.BoardGameItem
import de.bgs.secondary.database.GameFamily
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.commons.csv.CSVFormat
import org.springframework.stereotype.Service
import java.io.File

@Service
class CsvService(
    private val gameFamilyJpaRepo: GameFamilyJpaRepo,
    gitProperties: GitConfigurationProperties
) {
    val logger = KotlinLogging.logger {}
    val gameFamilyCsvFileName = gitProperties.gameFamilyCsvFileName
    val boardGameCsvFileName = gitProperties.boardGameCsvFileName

    fun parseGameFamily(repoDirectory: File): List<GameFamily> {
        return CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
            setIgnoreSurroundingSpaces(true)
        }.get()
            .parse(getFileReader(repoDirectory, gameFamilyCsvFileName))
            .drop(1) // Dropping the header
            .map {
                val gameFamily = GameFamily(
                    gameFamilyId = it[0].toLong(),
                    name = it[1]
                )
                logger.info { "Successfully parsed GameFamily with Id ${gameFamily.gameFamilyId}" }
                return@map gameFamily
            }

    }

    fun parseBoardGame(repoDirectory: File): List<BoardGameItem> { //
        return CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
            setIgnoreSurroundingSpaces(true)
        }.get()
            .parse(getFileReader(repoDirectory, boardGameCsvFileName))
            .drop(1) // Dropping the header
            .map {
                val boardGameItem = BoardGameItem(
                    bggId = it[0].toLong(),
                    name = it[1],
                    year = it[2].toIntOrNull(),
                    // game type [3]
                    // designer [4]
                    // artist [5]
                    // publisher [6]
                    minPlayers = it[7].toIntOrNull(),
                    maxPlayers = it[8].toIntOrNull(),
                    minPlayersRec = it[9].toIntOrNull(),
                    maxPlayersRec = it[10].toIntOrNull(),
                    minPlayersBest = it[11].toIntOrNull(),
                    maxPlayersBest = it[12].toIntOrNull(),
                    minAge = it[13].toIntOrNull(),
                    minAgeRec = it[14].toDoubleOrNull(),
                    minTime = it[15].toIntOrNull(),
                    maxTime = it[16].toIntOrNull(),
                    // category [17]
                    // mechanic [18]
                    cooperative = it[19].toBoolean(),
//                    compilation = it[20].toInt(),
//                    compilationOf = it[21],
                    family = getLinkedFamilies(it[22]),
//                    implementation = it[23],
//                    integration = it[24],
                    rank = it[25].toIntOrNull(),
                    numVotes = it[26].toIntOrNull(),
                    avgRating = it[27].toDoubleOrNull(),
                    stdDevRating = it[28].toDoubleOrNull(),
                    bayesRating = it[29].toDoubleOrNull(),
                    complexity = it[30].toDoubleOrNull(),
                    languageDependency = it[31].toDoubleOrNull()
                )
                logger.info { "Successfully parsed board Game with Id ${boardGameItem.bggId}" }
                return@map boardGameItem
            }
    }

    private fun getLinkedFamilies(familyIds: String): MutableSet<GameFamily> {
        if (familyIds.isEmpty()) return mutableSetOf()

        val familyIdList: List<Long> = familyIds.split(",").map { it.toLong() }
        return gameFamilyJpaRepo.findByGameFamilyIdIn(familyIdList)
    }

    fun getFileReader(rootDirectory: File, fileName: String) = rootDirectory.resolve(fileName).reader()
}