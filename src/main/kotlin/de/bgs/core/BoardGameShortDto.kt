package de.bgs.core

import de.bgs.secondary.database.BoardGameItem

data class BoardGameShortDto(
    val bggId: Long,
    val name: String
) {
    companion object {
        fun from(boardGameItems: List<BoardGameItem>): List<BoardGameShortDto> {
            return boardGameItems.map { BoardGameShortDto(it.bggId, it.name) }
        }
    }
}