package de.bgs.secondary.database

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import org.hibernate.Hibernate

@Entity
class BoardGameItem(
    @Id
    @GeneratedValue
    var technicalId: Long? = null,

    @Column(unique = true)
    var bggId: Long,

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
    var minAgeRec: Double,
    var minTime: Int,
    var maxTime: Int,

//    @ManyToMany
//    var category: MutableSet<GameCategory>
//
//    @ManyToMany
//    var mechanic: MutableSet<GameMechanic>

    var cooperative: Boolean,
    var compilation: Int,
//    var compilationOf: String, //ref what TODO with this? Link to bggId?

    @ManyToMany(cascade = [CascadeType.PERSIST])
    @JsonManagedReference
    var family: MutableSet<GameFamily>? = mutableSetOf(),

//    var implementation: String, //ref
//    var integration: String, //ref
    var rank: Int,
    var numVotes: Int,
    var avgRating: Double,
    var stdDevRating: Double,
    var bayesRating: Double,
    var complexity: Double,
    var languageDependency: Double
) {
    fun setGameFamily(gameFamily: MutableSet<GameFamily>) {
        this.family = gameFamily
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as BoardGameItem

        return technicalId == other.technicalId
    }

    override fun hashCode(): Int = javaClass.hashCode()
}