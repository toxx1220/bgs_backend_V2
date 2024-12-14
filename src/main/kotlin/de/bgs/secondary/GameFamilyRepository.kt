package de.bgs.secondary

import de.bgs.secondary.database.GameFamily
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameFamilyRepository : CrudRepository<GameFamily, Long> {
}