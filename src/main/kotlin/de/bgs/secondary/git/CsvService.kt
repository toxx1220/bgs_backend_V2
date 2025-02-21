package de.bgs.secondary.git

import de.bgs.secondary.database.*
import org.apache.commons.csv.CSVFormat
import org.springframework.stereotype.Service
import java.io.File
import java.util.stream.Stream

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

    fun parseBoardGamesStream(
        repoDirectory: File,
        gameFamilyMap: Map<Long, GameFamily>,
        gameTypeMap: Map<Long, GameType>,
        personMap: Map<Long, Person>,
        gameCategoryMap: Map<Long, GameCategory>,
        mechanicMap: Map<Long, Mechanic>,
        publisherMap: Map<Long, Publisher>
    ): Stream<BoardGameItem> { //
        return CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
            setIgnoreSurroundingSpaces(true)
        }.get()
            .parse(getFileReader(repoDirectory, boardGameCsvFileName))
            .drop(1) // Dropping the header
            .map {
                BoardGameItem(
                    bggId = it[0].toLong(),
                    name = it[1],
                    year = it[2].toIntOrNull(),
                    gameTypes = getMatchingBggEntity(it[3], gameTypeMap),
                    designer = getMatchingBggEntity(it[4], personMap),
                    artist = getMatchingBggEntity(it[5], personMap),
                    publisher = getMatchingBggEntity(it[6], publisherMap),
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
                    category = getMatchingBggEntity(it[17], gameCategoryMap),
                    mechanic = getMatchingBggEntity(it[18], mechanicMap),
                    cooperative = it[19].toBoolean(),
//                    compilation = it[20].toInt(),
//                    compilationOf = it[21],
                    gameFamilies = getMatchingBggEntity(it[22], gameFamilyMap),
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
            }
            .stream()
    }

    fun parseGameFamily(repoDirectory: File): List<GameFamily> {
        return CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
            setIgnoreSurroundingSpaces(true)
        }.get()
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
        return CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
            setIgnoreSurroundingSpaces(true)
        }.get()
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
        return CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
            setIgnoreSurroundingSpaces(true)
        }.get()
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
        return CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
            setIgnoreSurroundingSpaces(true)
        }.get()
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

    fun parseMechanic(repoDirectory: File): List<Mechanic> {
        return CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
            setIgnoreSurroundingSpaces(true)
        }.get()
            .parse(getFileReader(repoDirectory, mechanicCsvFileName))
            .drop(1) // Dropping the header
            .map {
                val mechanic = Mechanic(
                    bggId = it[0].toLong(),
                    name = it[1]
                )
                return@map mechanic
            }
    }

    fun parsePublisher(repoDirectory: File): List<Publisher> {
        return CSVFormat.Builder.create(CSVFormat.DEFAULT).apply {
            setIgnoreSurroundingSpaces(true)
        }.get()
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

    private fun <T : BaseEntity> getMatchingBggEntity(bggId: String, bggEntityMap: Map<Long, T>): MutableSet<T> {
        if (bggId.isEmpty()) return mutableSetOf()

        val bggEntityList: List<Long> = bggId.split(",").map { it.toLong() }
        return bggEntityMap.filterKeys { bggEntityList.contains(it) }.values.toMutableSet()
    }

    fun getFileReader(rootDirectory: File, fileName: String) = rootDirectory.resolve(fileName).reader()
}