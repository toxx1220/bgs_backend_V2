package de.bgs.secondary.database

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany

@Entity
class GameFamily(
    @Id
    val id: Long,
    var name: String
) {
    @ManyToMany
    var boardGame: MutableSet<BoardGameItem> = mutableSetOf()
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
