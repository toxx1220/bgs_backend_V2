package de.bgs.secondary.database

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany

@Entity
class BoardGameItem {
    @Id
    private var bggId: Long? = null

    private var name: String? = null
    private var year: Int? = null

    @ManyToMany
    private var gameType: Set<GameType>? = null

    @ManyToMany
    private var designer: Set<GamePerson>? = null

    @ManyToMany
    private var artist: Set<GamePerson>? = null

    @ManyToMany
    private var publisher: Set<GamePublisher>? = null

    private var minPlayers: Int? = null
    private var maxPlayers: Int? = null
    private var minPlayersRec: Int? = null
    private var maxPlayersRec: Int? = null
    private var minPlayersBest: Int? = null
    private var maxPlayersBest: Int? = null
    private var minAge: Int? = null
    private var minAgeRec: Int? = null
    private var minTime: Int? = null
    private var maxTime: Int? = null

    @ManyToMany
    private var category: Set<GameCategory>? = null

    @ManyToMany
    private var mechanic: Set<GameMechanic>? = null

    private var cooperative = false
    private var compilation = false
    private var compilationOf: String? = null //ref what TODO with this? Link to bggId?

    @ManyToMany
    private var family: Set<GameFamily>? = null

    private var implementation: String? = null //ref
    private var integration: String? = null //ref
    private var rank: Int? = null
    private var numVotes: Int? = null
    private var avgRating: Double? = null
    private var stdDevRating: Double? = null
    private var bayesRating: Double? = null
    private var complexity: Double? = null
    private var languageDependency: Double? = null
}