package de.bgs.secondary.database

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany

@Entity
class BoardGameItem (
    @Id
    var bggId: Long? = null,

    var name: String,
    var year: Int,

//    @ManyToMany
//    var gameType: MutableSet<GameType>?
//
//    @ManyToMany
//    var designer: MutableSet<GamePerson>
//
//    @ManyToMany
//    var artist: MutableSet<GamePerson>
//
//    @ManyToMany
//    var publisher: MutableSet<GamePublisher>

    var minPlayers: Int,
    var maxPlayers: Int,
    var minPlayersRec: Int,
    var maxPlayersRec: Int,
    var minPlayersBest: Int,
    var maxPlayersBest: Int,
    var minAge: Int,
    var minAgeRec: Int,
    var minTime: Int,
    var maxTime: Int,

//    @ManyToMany
//    var category: MutableSet<GameCategory>
//
//    @ManyToMany
//    var mechanic: MutableSet<GameMechanic>

    var cooperative: Boolean,
    var compilation: Boolean,
    var compilationOf: String, //ref what TODO with this? Link to bggId?

    @ManyToMany(mappedBy = "boardGame")
    var family: MutableSet<GameFamily>,

    var implementation: String, //ref
    var integration: String, //ref
    var rank: Int,
    var numVotes: Int,
    var avgRating: Double,
    var stdDevRating: Double,
    var bayesRating: Double,
    var complexity: Double,
    var languageDependency: Double
)