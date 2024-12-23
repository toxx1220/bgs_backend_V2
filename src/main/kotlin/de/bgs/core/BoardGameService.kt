package de.bgs.core

import de.bgs.secondary.BoardGameJpaRepo
import de.bgs.secondary.database.BoardGameItem
import org.springframework.stereotype.Service

@Service
class BoardGameService(private val boardGameJpaRepo: BoardGameJpaRepo) {
    fun getBoardGameItems(): MutableList<BoardGameItem> = boardGameJpaRepo.findAll()
}