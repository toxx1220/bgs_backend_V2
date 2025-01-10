package de.bgs.secondary

import de.bgs.secondary.database.BoardGameItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface BoardGameJpaRepo : JpaSpecificationExecutor<BoardGameItem>, JpaRepository<BoardGameItem, Long> {
    fun findByBggId(bggId: Long): Optional<BoardGameItem>
}