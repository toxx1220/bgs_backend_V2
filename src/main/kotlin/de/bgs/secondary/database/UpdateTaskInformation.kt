package de.bgs.secondary.database

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class UpdateTaskInformation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    // saved as UTC. Instant has only experimental support
    var lastUpdateTaskTime: LocalDateTime? = null,

    var executionDurationInMinutes : Long? = null
)