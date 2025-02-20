package de.bgs.primary

import de.bgs.core.BoardGameService
import de.bgs.core.FilterCondition
import de.bgs.secondary.database.BoardGameItem
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
class BoardGameController(private val boardGameService: BoardGameService) {

    @GetMapping
    fun getHello(): String {
        return "Hello. It is ${LocalDateTime.now()}"
    }

    @GetMapping("/boardgame")
    fun getBoardGame(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int,
    ): List<BoardGameItemDto> {
        val pageRequest = PageRequest.of(pageNumber, pageSize)
        val boardGames: Page<BoardGameItem> = boardGameService.getBoardGameItems(pageRequest)
        return listOf(BoardGameItemDto(boardGames.content, pageNumber, pageSize, boardGames.totalElements))
    }

    @PostMapping("/boardgame")
    fun filterBoardGame(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int,
        @RequestBody(required = false) filterConditions: Set<FilterCondition> = emptySet(),
    ): List<BoardGameItemDto> {
        val pageRequest = PageRequest.of(pageNumber, pageSize)
        val boardGames: Page<BoardGameItem> = boardGameService.filterBoardGameItems(filterConditions, pageRequest)
        return listOf(BoardGameItemDto(boardGames.content, pageNumber, pageSize, boardGames.totalElements))
    }
}