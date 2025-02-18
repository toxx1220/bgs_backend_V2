package de.bgs.core

import de.bgs.secondary.database.*
import jakarta.persistence.metamodel.Attribute


enum class BoardGameField(val fieldPath: List<Attribute<*, *>>) {
    TECHNICAL_ID(listOf(BoardGameItem_.technicalId)),
    BGG_ID(listOf(BoardGameItem_.bggId)),
    NAME(listOf(BoardGameItem_.name)),
    YEAR(listOf(BoardGameItem_.year)),
    GAME_TYPE_NAME(listOf(BoardGameItem_.gameTypes, GameType_.name)),
    DESIGNER_NAME(listOf(BoardGameItem_.designer, Person_.name)),
    ARTIST_NAME(listOf(BoardGameItem_.artist, Person_.name)),
    PUBLISHER_NAME(listOf(BoardGameItem_.publisher, Publisher_.name)),
    MIN_PLAYERS(listOf(BoardGameItem_.minPlayers)),
    MAX_PLAYERS(listOf(BoardGameItem_.maxPlayers)),
    MIN_PLAYERS_REC(listOf(BoardGameItem_.minPlayersRec)),
    MAX_PLAYERS_REC(listOf(BoardGameItem_.maxPlayersRec)),
    MIN_PLAYERS_BEST(listOf(BoardGameItem_.minPlayersBest)),
    MAX_PLAYERS_BEST(listOf(BoardGameItem_.maxPlayersBest)),
    MIN_AGE(listOf(BoardGameItem_.minAge)),
    MIN_AGE_REC(listOf(BoardGameItem_.minAgeRec)),
    MIN_TIME(listOf(BoardGameItem_.minTime)),
    MAX_TIME(listOf(BoardGameItem_.maxTime)),
    CATEGORY_NAME(listOf(BoardGameItem_.category, Category_.name)),
    MECHANIC_NAME(listOf(BoardGameItem_.mechanic, Mechanic_.name)),
    COOPERATIVE(listOf(BoardGameItem_.cooperative)),
    GAME_FAMILY_NAME(listOf(BoardGameItem_.gameFamilies, GameFamily_.name)),
    RANK(listOf(BoardGameItem_.rank)),
    NUM_VOTES(listOf(BoardGameItem_.numVotes)),
    AVG_RATING(listOf(BoardGameItem_.avgRating)),
    STD_DEV_RATING(listOf(BoardGameItem_.stdDevRating)),
    BAYES_RATING(listOf(BoardGameItem_.bayesRating)),
    COMPLEXITY(listOf(BoardGameItem_.complexity)),
    LANGUAGE_DEPENDENCY(listOf(BoardGameItem_.languageDependency));

    fun getField(): Attribute<*, *> {
        return fieldPath[fieldPath.lastIndex]
    }

    fun getFieldName(): String {
        return fieldPath[fieldPath.lastIndex].name
    }

    fun getJoinPath(): List<Attribute<*, *>> {
        return fieldPath.subList(0, fieldPath.lastIndex)
    }
}

