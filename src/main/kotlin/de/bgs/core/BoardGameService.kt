package de.bgs.core

import de.bgs.secondary.BoardGameJpaRepo
import de.bgs.secondary.BoardGameSpecification
import de.bgs.secondary.database.BoardGameItem
import de.bgs.secondary.metadata.MetaDataService
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import kotlin.concurrent.thread

@Service
class BoardGameService(
    private val boardGameJpaRepo: BoardGameJpaRepo,
    private val entityManager: EntityManager,
    private val metaDataService: MetaDataService
) {
    private val logger = KotlinLogging.logger { }

    fun getBoardGameItems(pageRequest: PageRequest, loadMetaData: Boolean): Page<BoardGameItem> {
        val boardGames = boardGameJpaRepo.findAll(pageRequest)
        val updatedBoardGames =
            if (loadMetaData) updateBoardGames(boardGames.content) else boardGames.content
        return PageImpl(
            updatedBoardGames, pageRequest, boardGames.totalElements
        )
    }

    fun saveBoardGame(boardGameItem: BoardGameItem): BoardGameItem = boardGameJpaRepo.save(boardGameItem)

    fun filterBoardGameItems(
        filterRequest: FilterRequest,
        pageRequest: PageRequest,
        loadMetaData: Boolean
    ): Page<BoardGameItem> {
        val specification = BoardGameSpecification(filterRequest)
        val boardGameItemPage: Page<BoardGameItem> = boardGameJpaRepo.findAll(specification, pageRequest)
        val updatedBoardGames =
            if (loadMetaData) updateBoardGames(boardGameItemPage.content) else boardGameItemPage.content
        return PageImpl(
            updatedBoardGames, pageRequest, boardGameItemPage.totalElements
        )
    }

    fun updateBoardGames(boardGameItemList: List<BoardGameItem>): List<BoardGameItem> {
        val updatedBoardGames = metaDataService.retrieveMetaData(boardGameItemList)
        thread { // asynchronously update board games
            updatedBoardGames.forEach { boardGameItem ->
                try {
                    saveBoardGame(boardGameItem)
                    logger.info { "Updated board game with ID: ${boardGameItem.bggId}" }
                } catch (e: Exception) {
                    logger.error(e) { "Failed to update board game with ID: ${boardGameItem.bggId}" }
                }
            }
        }
        return updatedBoardGames
    }

    @Transactional
    fun clearDatabase() {
        logger.info { "Clearing Database!" }
        boardGameJpaRepo.truncateWithCascade()
        entityManager.clear()
    }
}
