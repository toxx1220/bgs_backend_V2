package de.bgs.util

import de.bgs.secondary.database.BoardGameItem
import kotlin.random.Random

class BoardGameTestData {
    companion object {
        fun testBoardGame(): BoardGameItem {
            return BoardGameItem(
                bggId = Random.nextLong(),
                name = "Test Game",
                year = 2021,
                minPlayers = 2,
                maxPlayers = 4,
                minPlayersRec = 2,
                maxPlayersRec = 4,
                minPlayersBest = 2,
                maxPlayersBest = 4,
                minAge = 8,
                minAgeRec = 8.0,
                minTime = 30,
                maxTime = 60,
                cooperative = false,
                description = "test-description",
                rank = 1,
                numVotes = 1,
                avgRating = 1.0,
                stdDevRating = 0.0,
                bayesRating = 1.0,
                complexity = 1.0,
                languageDependency = 1.0,
                imageUri = "test-image-uri",
            )
        }
    }
}