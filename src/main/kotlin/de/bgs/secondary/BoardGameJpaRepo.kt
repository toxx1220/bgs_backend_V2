package de.bgs.secondary

import de.bgs.secondary.database.BoardGameItem
import de.bgs.secondary.database.GameFamily
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BoardGameJpaRepo : JpaSpecificationExecutor<BoardGameItem>, JpaRepository<BoardGameItem, Long> {
    fun findByBggId(bggId: Long): Optional<BoardGameItem>
}

@Repository
interface GameFamilyJpaRepo : JpaRepository<GameFamily, Long> {
    fun findByBggId(gameFamilyId: Long): Optional<GameFamily>
    fun findByBggIdIn(gameFamilyIds: List<Long>): MutableSet<GameFamily>
}