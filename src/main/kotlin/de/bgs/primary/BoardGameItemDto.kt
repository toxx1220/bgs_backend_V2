package de.bgs.primary

import de.bgs.secondary.database.BoardGameItem

data class BoardGameItemDto(
    val boardGameItems: List<BoardGameItem>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Long
) 