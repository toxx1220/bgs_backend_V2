package de.bgs.core

import de.bgs.secondary.database.*
import jakarta.persistence.metamodel.SetAttribute
import jakarta.persistence.metamodel.SingularAttribute

enum class BoardGameField(
    val searchField: SingularAttribute<*, *>,
    val joinAttribute: SetAttribute<BoardGameItem, out BaseEntity>? = null
) {
    TECHNICAL_ID(BoardGameItem_.technicalId, null),
    BGG_ID(BoardGameItem_.bggId, null),
    NAME(BoardGameItem_.name, null),
    YEAR(BoardGameItem_.year, null),
    GAME_TYPE_NAME(GameType_.name, BoardGameItem_.gameTypes),
    DESIGNER_NAME(Person_.name, BoardGameItem_.designer),
    ARTIST_NAME(Person_.name, BoardGameItem_.artist),
    PUBLISHER_NAME(Publisher_.name, BoardGameItem_.publisher),
    MIN_PLAYERS(BoardGameItem_.minPlayers),
    MAX_PLAYERS(BoardGameItem_.maxPlayers),
    MIN_PLAYERS_REC(BoardGameItem_.minPlayersRec),
    MAX_PLAYERS_REC(BoardGameItem_.maxPlayersRec),
    MIN_PLAYERS_BEST(BoardGameItem_.minPlayersBest),
    MAX_PLAYERS_BEST(BoardGameItem_.maxPlayersBest),
    MIN_AGE(BoardGameItem_.minAge),
    MIN_AGE_REC(BoardGameItem_.minAgeRec),
    MIN_TIME(BoardGameItem_.minTime),
    MAX_TIME(BoardGameItem_.maxTime),
    CATEGORY_NAME(GameCategory_.name, BoardGameItem_.category),
    MECHANIC_NAME(GameMechanic_.name, BoardGameItem_.mechanic),
    COOPERATIVE(BoardGameItem_.cooperative),
    GAME_FAMILY_NAME(GameFamily_.name, BoardGameItem_.gameFamilies),
    RANK(BoardGameItem_.rank),
    NUM_VOTES(BoardGameItem_.numVotes),
    AVG_RATING(BoardGameItem_.avgRating),
    STD_DEV_RATING(BoardGameItem_.stdDevRating),
    BAYES_RATING(BoardGameItem_.bayesRating),
    COMPLEXITY(BoardGameItem_.complexity),
    LANGUAGE_DEPENDENCY(BoardGameItem_.languageDependency);
}

