package de.bgs.core

import de.bgs.secondary.BoardGameJpaRepo
import de.bgs.secondary.database.BoardGameItem
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.Objects.isNull

@Service
class BoardGameService(private val boardGameJpaRepo: BoardGameJpaRepo) {
    private val logger = KotlinLogging.logger {}

    fun getBoardGameItems(pageRequest: PageRequest): Page<BoardGameItem> = boardGameJpaRepo.findAll(pageRequest)

    fun saveBoardGame(boardGameItem: BoardGameItem): BoardGameItem {
        val boardGameItemOpt = boardGameJpaRepo.findByBggId(boardGameItem.bggId)
        if (isNull(boardGameItem.technicalId)) {
            boardGameItemOpt.ifPresent { boardGameItem.technicalId = it.technicalId }
        } else if (boardGameItemOpt.isPresent && boardGameItemOpt.get().bggId != boardGameItem.bggId) {
            throw IllegalArgumentException("bggId ${boardGameItem.bggId} is not the same as in the database ${boardGameItemOpt.get().bggId}")
        }
        return boardGameJpaRepo.save(boardGameItem)
    }

    fun saveBoardGames(boardGameItemList: List<BoardGameItem>): List<BoardGameItem> {
        return boardGameItemList
            .mapNotNull { boardGameItem ->
                try {
                    saveBoardGame(boardGameItem)
                } catch (e: Exception) {
                    logger.error(e) { "Could not save BoardGameItem ${boardGameItem.bggId}: ${e.message}" }
                    null
                }
            }
    }
}
