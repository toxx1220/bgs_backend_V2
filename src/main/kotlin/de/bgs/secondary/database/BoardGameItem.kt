package de.bgs.secondary.database

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany

@Entity
class BoardGameItem {
    @Id
    private var bggId: Long = 0

    private var name: String? = null
    private var year: Int? = null

//    @ManyToMany
//    private var gameType: MutableSet<GameType>?
//
//    @ManyToMany
//    private var designer: MutableSet<GamePerson>? = null
//
//    @ManyToMany
//    private var artist: MutableSet<GamePerson>? = null
//
//    @ManyToMany
//    private var publisher: MutableSet<GamePublisher>? = null

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

//    @ManyToMany
//    private var category: MutableSet<GameCategory>? = null
//
//    @ManyToMany
//    private var mechanic: MutableSet<GameMechanic>? = null

    private var cooperative = false
    private var compilation = false
    private var compilationOf: String? = null //ref what TODO with this? Link to bggId?

    @ManyToMany(mappedBy = "boardGame")
    private var family: MutableSet<GameFamily>? = null

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