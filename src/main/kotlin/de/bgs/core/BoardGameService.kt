package de.bgs.core

import de.bgs.secondary.BoardGameJpaRepo
import de.bgs.secondary.BoardGameSpecification
import de.bgs.secondary.database.BoardGameItem
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class BoardGameService(
    private val boardGameJpaRepo: BoardGameJpaRepo,
    private val entityManager: EntityManager
) {
    private val logger = KotlinLogging.logger { }
    fun getBoardGameItems(pageRequest: PageRequest): Page<BoardGameItem> = boardGameJpaRepo.findAll(pageRequest)

    fun saveBoardGame(boardGameItem: BoardGameItem): BoardGameItem = boardGameJpaRepo.save(boardGameItem)

    fun filterBoardGameItems(filterRequest: FilterRequest, pageRequest: PageRequest): Page<BoardGameItem> {
        val specification = BoardGameSpecification(filterRequest)
        return boardGameJpaRepo.findAll(specification, pageRequest)
    }

    @Transactional
    fun clearDatabase() {
        logger.info { "Clearing Database!" }
        boardGameJpaRepo.truncateWithCascade()
        entityManager.clear()
    }
}
