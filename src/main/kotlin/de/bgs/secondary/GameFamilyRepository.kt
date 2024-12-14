package de.bgs.secondary

import de.bgs.secondary.database.GameFamily
import org.springframework.data.repository.CrudRepository

interface GameFamilyRepository : CrudRepository<GameFamily, Long> {
}