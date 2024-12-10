package de.bgs.secondary.database

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany

@Entity
class GameFamily {
    @Id
    var id: Long? = null
    var name: String? = null

    @ManyToMany()
    var boardGame: Set<BoardGameItem>? = null
}