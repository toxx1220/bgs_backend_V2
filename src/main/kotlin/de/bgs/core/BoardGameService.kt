package de.bgs.core

import de.bgs.secondary.BoardGameJpaRepo
import de.bgs.secondary.BoardGameSpecification
import de.bgs.secondary.database.BoardGameItem
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
    fun getBoardGameItems(pageRequest: PageRequest): Page<BoardGameItem> = boardGameJpaRepo.findAll(pageRequest)

    fun saveBoardGame(boardGameItem: BoardGameItem): BoardGameItem = boardGameJpaRepo.save(boardGameItem)

    fun filterBoardGameItems(filterConditions: Set<FilterCondition>, pageRequest: PageRequest): Page<BoardGameItem> {
        val specification = BoardGameSpecification(filterConditions)
        return boardGameJpaRepo.findAllBy(specification, pageRequest)
    }

    @Transactional
    fun deleteDatabase() {
        boardGameJpaRepo.truncateWithCascade()
        entityManager.clear()
    }
}
