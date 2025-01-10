package de.bgs.core

import de.bgs.secondary.BoardGameJpaRepo
import de.bgs.secondary.database.BoardGameItem
import org.springframework.stereotype.Service
import java.util.Objects.isNull

@Service
class BoardGameService(private val boardGameJpaRepo: BoardGameJpaRepo) {

    fun getBoardGameItems(): MutableList<BoardGameItem> = boardGameJpaRepo.findAll()

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
        return boardGameItemList.stream().map { saveBoardGame(it) }.toList()
    }
}