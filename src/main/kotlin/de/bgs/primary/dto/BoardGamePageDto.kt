package de.bgs.primary.dto

import de.bgs.secondary.database.BoardGameItem

data class BoardGamePageDto(
    val boardGameItems: List<BoardGameItem> = emptyList(),
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Long
) 