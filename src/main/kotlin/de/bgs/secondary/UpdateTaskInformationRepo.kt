package de.bgs.secondary

import de.bgs.secondary.database.UpdateTaskInformation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UpdateTaskInformationRepo : JpaRepository<UpdateTaskInformation, Long> {
    fun findTopByOrderByLastUpdateTaskTimeDesc(): UpdateTaskInformation?
}