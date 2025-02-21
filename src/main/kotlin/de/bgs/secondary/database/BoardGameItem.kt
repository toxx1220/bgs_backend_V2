package de.bgs.secondary.database

import jakarta.persistence.*
import org.hibernate.Hibernate

@Entity
class BoardGameItem(
    @Id
    @GeneratedValue
    var technicalId: Long? = null,

    @Column(unique = true)
    var bggId: Long,

    var name: String = "",
    var year: Int? = null,

    @ManyToMany
    var gameTypes: MutableSet<GameType>? = mutableSetOf(),

    @ManyToMany
    var designer: MutableSet<Person>? = mutableSetOf(),

    @ManyToMany
    var artist: MutableSet<Person>? = mutableSetOf(),

    @ManyToMany
    var publisher: MutableSet<Publisher>? = mutableSetOf(),

    var minPlayers: Int? = null,
    var maxPlayers: Int? = null,
    var minPlayersRec: Int? = null,
    var maxPlayersRec: Int? = null,
    var minPlayersBest: Int? = null,
    var maxPlayersBest: Int? = null,
    var minAge: Int? = null,
    var minAgeRec: Double? = null,
    var minTime: Int? = null,
    var maxTime: Int? = null,

    @ManyToMany
    var category: MutableSet<GameCategory>? = mutableSetOf(),

    @ManyToMany
    var mechanic: MutableSet<Mechanic>? = mutableSetOf(),

    var cooperative: Boolean = false,

//    var compilation: Int, TODO: ref
//    var compilationOf: String, //ref what TODO with this? Link to bggId?

    @ManyToMany
    var gameFamilies: MutableSet<GameFamily>? = mutableSetOf(),

//    var implementation: String, //ref
//    var integration: String, //ref

    var rank: Int? = null,
    var numVotes: Int? = null,
    var avgRating: Double? = null,
    var stdDevRating: Double? = null,
    var bayesRating: Double? = null,
    var complexity: Double? = null,
    var languageDependency: Double? = null
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as BoardGameItem

        return technicalId == other.technicalId
    }

    override fun hashCode(): Int = javaClass.hashCode()
}