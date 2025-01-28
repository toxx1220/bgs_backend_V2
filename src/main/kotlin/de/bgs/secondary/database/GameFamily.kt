package de.bgs.secondary.database

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import org.hibernate.Hibernate

@Entity
class GameFamily(
    @Id
    @GeneratedValue
    var technicalId: Long? = null,

    @Column(unique = true)
    val gameFamilyId: Long,

    var name: String,

    @ManyToMany
    @JsonBackReference
    var boardGame: MutableSet<BoardGameItem> = mutableSetOf()
) {
    fun addBoardGames(boardGameItem: MutableSet<BoardGameItem>) {
        boardGame.addAll(boardGameItem)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as GameFamily

        return technicalId == other.technicalId
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
/*
class GameFamily {
    @Id
    @Column(nullable = false)
    val id: BigDecimal,
    var name: String = ""

    @ManyToMany
    var boardGame: MutableSet<BoardGameItem> = mutableSetOf()

}*/
