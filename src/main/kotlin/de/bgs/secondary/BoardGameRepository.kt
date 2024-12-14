package de.bgs.secondary

import de.bgs.secondary.database.BoardGameItem
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardGameRepository : CrudRepository<BoardGameItem, Long> {
}