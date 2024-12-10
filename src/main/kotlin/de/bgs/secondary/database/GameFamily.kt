package de.bgs.secondary.database

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany

@Entity
class GameFamily {
    @Id
    var id: Long = 0
    var name: String = ""

    @ManyToMany
    var boardGame: MutableSet<BoardGameItem> = mutableSetOf()
}