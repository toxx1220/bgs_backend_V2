package de.bgs.secondary.git

import de.bgs.secondary.*
import de.bgs.secondary.database.*
import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.commons.csv.CSVFormat
import org.springframework.stereotype.Service
import java.io.File

@Service
class CsvService(
    private val gameFamilyJpaRepo: GameFamilyJpaRepo,
    gitProperties: GitConfigurationProperties,
    private val gameTypeJpaRepo: GameTypeJpaRepo,
    private val personJpaRepo: PersonJpaRepo,
    private val categoryJpaRepo: CategoryJpaRepo,
    private val mechanicJpaRepo: MechanicJpaRepo,
    private val publisherJpaRepo: PublisherJpaRepo
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
                    bggId = it[0].toLong(),
                    name = it[1]
                )
                logger.info { "Successfully parsed GameFamily with Id ${gameFamily.bggId}" }
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
                    gameTypes = getTypes(it[3]),
                    designer = getPersonas(it[4]),
                    artist = getPersonas(it[5]),
                    publisher = getPublishers(it[6]),
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
                    category = getCategories(it[17]),
                    mechanic = getMechanic(it[18]),
                    cooperative = it[19].toBoolean(),
//                    compilation = it[20].toInt(),
//                    compilationOf = it[21],
                    gameFamilies = getFamilies(it[22]),
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

    private fun getFamilies(bggIds: String): MutableSet<GameFamily> {
        if (bggIds.isEmpty()) return mutableSetOf()

        val familyIdList: List<Long> = bggIds.split(",").map { it.toLong() }
        return gameFamilyJpaRepo.findByBggIdIn(familyIdList)
    }


    private fun getTypes(bggIds: String): MutableSet<GameType> {
        if (bggIds.isEmpty()) return mutableSetOf()

        val types: List<Long> = bggIds.split(",").map { it.toLong() }
        return gameTypeJpaRepo.findByBggIdIn(types)
    }


    private fun getPersonas(bggIds: String): MutableSet<Person> {
        if (bggIds.isEmpty()) return mutableSetOf()

        val personList: List<Long> = bggIds.split(",").map { it.toLong() }
        return personJpaRepo.findByBggIdIn(personList)
    }


    private fun getCategories(bggIds: String): MutableSet<Category> {
        if (bggIds.isEmpty()) return mutableSetOf()

        val categoryList: List<Long> = bggIds.split(",").map { it.toLong() }
        return categoryJpaRepo.findByBggIdIn(categoryList)
    }


    private fun getMechanic(bggIds: String): MutableSet<Mechanic> {
        if (bggIds.isEmpty()) return mutableSetOf()

        val mechanicList: List<Long> = bggIds.split(",").map { it.toLong() }
        return mechanicJpaRepo.findByBggIdIn(mechanicList)
    }

    private fun getPublishers(bggIds: String): MutableSet<Publisher> {
        if (bggIds.isEmpty()) return mutableSetOf()

        val publisherList: List<Long> = bggIds.split(",").map { it.toLong() }
        return publisherJpaRepo.findByBggIdIn(publisherList)
    }

    fun getFileReader(rootDirectory: File, fileName: String) = rootDirectory.resolve(fileName).reader()
}