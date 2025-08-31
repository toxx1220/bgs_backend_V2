package de.bgs.primary.dto

import de.bgs.core.BoardGameShortDto

data class BoardGameShortPageDto(
    val boardGameItems: List<BoardGameShortDto> = emptyList(),
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Long
)