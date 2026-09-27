package de.bgs.secondary.git

import de.bgs.secondary.database.BaseEntity
import de.bgs.secondary.database.BoardGameItem
import de.bgs.secondary.database.GameCategory
import de.bgs.secondary.database.GameFamily
import de.bgs.secondary.database.GameMechanic
import de.bgs.secondary.database.GameType
import de.bgs.secondary.database.Person
import de.bgs.secondary.database.Publisher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.springframework.stereotype.Service
import java.io.File

@Service
class CsvService(
    gitProperties: GitConfigurationProperties,
) {
    val gameFamilyCsvFileName = gitProperties.gameFamilyCsvFileName
    val boardGameCsvFileName = gitProperties.boardGameCsvFileName
    val gameTypeCsvFileName = gitProperties.gameTypeCsvFileName
    val personCsvFileName = gitProperties.personCsvFileName
    val categoryCsvFileName = gitProperties.categoryCsvFileName
    val mechanicCsvFileName = gitProperties.mechanicCsvFileName
    val publisherCsvFileName = gitProperties.publisherCsvFileName

    fun parseBoardGames(
        repoDirectory: File,
        entityMaps: BoardGameItemEntityRelations
    ): Flow<BoardGameItem> = flow {
        csvFormat()
            .parse(getFileReader(repoDirectory, boardGameCsvFileName)).use { parser ->
                parser.asSequence()
                    .drop(1) // Dropping the header
                    .forEach { emit(it.toBoardGameItem(entityMaps)) }
            }
    }

    fun parseGameFamily(repoDirectory: File): List<GameFamily> {
        return csvFormat()
            .parse(getFileReader(repoDirectory, gameFamilyCsvFileName))
            .drop(1) // Dropping the header
            .map {
                val gameFamily = GameFamily(
                    bggId = it[0].toLong(),
                    name = it[1]
                )
                return@map gameFamily
            }
    }

    fun parseGameType(repoDirectory: File): List<GameType> {
        return csvFormat()
            .parse(getFileReader(repoDirectory, gameTypeCsvFileName))
            .drop(1) // Dropping the header
            .map {
                val gameType = GameType(
                    bggId = it[0].toLong(),
                    name = it[1]
                )
                return@map gameType
            }
    }

    fun parsePerson(repoDirectory: File): List<Person> {
        return csvFormat()
            .parse(getFileReader(repoDirectory, personCsvFileName))
            .drop(1) // Dropping the header
            .map {
                val person = Person(
                    bggId = it[0].toLong(),
                    name = it[1]
                )
                return@map person
            }
    }

    fun parseCategory(repoDirectory: File): List<GameCategory> {
        return csvFormat()
            .parse(getFileReader(repoDirectory, categoryCsvFileName))
            .drop(1) // Dropping the header
            .map {
                val gameCategory = GameCategory(
                    bggId = it[0].toLong(),
                    name = it[1]
                )
                return@map gameCategory
            }
    }

    fun parseMechanic(repoDirectory: File): List<GameMechanic> {
        return csvFormat()
            .parse(getFileReader(repoDirectory, mechanicCsvFileName))
            .drop(1) // Dropping the header
            .map {
                val gameMechanic = GameMechanic(
                    bggId = it[0].toLong(),
                    name = it[1]
                )
                return@map gameMechanic
            }
    }

    fun parsePublisher(repoDirectory: File): List<Publisher> {
        return csvFormat()
            .parse(getFileReader(repoDirectory, publisherCsvFileName))
            .drop(1) // Dropping the header
            .map {
                val publisher = Publisher(
                    bggId = it[0].toLong(),
                    name = it[1]
                )
                return@map publisher
            }
    }

    private fun CSVRecord.toBoardGameItem(
        entityMaps: BoardGameItemEntityRelations
    ): BoardGameItem {
        return BoardGameItem(
            bggId = this[0].toLong(),
            name = this[1],
            year = this[2].toIntOrNull(),
            gameTypes = getMatchingBggEntity(this[3], entityMaps.gameTypeMap),
            designer = getMatchingBggEntity(this[4], entityMaps.personMap),
            artist = getMatchingBggEntity(this[5], entityMaps.personMap),
            publisher = getMatchingBggEntity(this[6], entityMaps.publisherMap),
            minPlayers = this[7].toIntOrNull(),
            maxPlayers = this[8].toIntOrNull(),
            minPlayersRec = this[9].toIntOrNull(),
            maxPlayersRec = this[10].toIntOrNull(),
            minPlayersBest = this[11].toIntOrNull(),
            maxPlayersBest = this[12].toIntOrNull(),
            minAge = this[13].toIntOrNull(),
            minAgeRec = this[14].toDoubleOrNull(),
            minTime = this[15].toIntOrNull(),
            maxTime = this[16].toIntOrNull(),
            category = getMatchingBggEntity(this[17], entityMaps.gameCategoryMap),
            mechanic = getMatchingBggEntity(this[18], entityMaps.gameMechanicMap),
            cooperative = this[19].toBoolean(),
//                    compilation = this[20].toInt(),
//                    compilationOf = this[21],
            gameFamilies = getMatchingBggEntity(this[22], entityMaps.gameFamilyMap),
//                    implementation = this[23],
//                    integration = this[24],
            rank = this[25].toIntOrNull(),
            numVotes = this[26].toIntOrNull(),
            avgRating = this[27].toDoubleOrNull(),
            stdDevRating = this[28].toDoubleOrNull(),
            bayesRating = this[29].toDoubleOrNull(),
            complexity = this[30].toDoubleOrNull(),
            languageDependency = this[31].toDoubleOrNull()
        )
    }

    private fun <T : BaseEntity> getMatchingBggEntity(bggId: String, bggEntityMap: Map<Long, T>): MutableSet<T> {
        if (bggId.isEmpty()) return mutableSetOf()

        val bggEntityList: List<Long> = bggId.split(",").map { it.toLong() }
        return bggEntityMap.filterKeys { bggEntityList.contains(it) }.values.toMutableSet()
    }

    fun getFileReader(rootDirectory: File, fileName: String) = rootDirectory.resolve(fileName).reader()
}

class BoardGameItemEntityRelations(
    val gameFamilyMap: Map<Long, GameFamily>,
    val gameTypeMap: Map<Long, GameType>,
    val personMap: Map<Long, Person>,
    val gameCategoryMap: Map<Long, GameCategory>,
    val gameMechanicMap: Map<Long, GameMechanic>,
    val publisherMap: Map<Long, Publisher>
)

private fun csvFormat(): CSVFormat = CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
    setIgnoreSurroundingSpaces(true)
}.get()