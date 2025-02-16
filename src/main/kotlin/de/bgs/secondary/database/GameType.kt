package de.bgs.secondary.database

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class GameType(
    @Id
    @GeneratedValue
    override var technicalId: Long? = null,

    @Column(unique = true)
    override var bggId: Long,

    override var name: String = "",

    ) : BaseEntity()
