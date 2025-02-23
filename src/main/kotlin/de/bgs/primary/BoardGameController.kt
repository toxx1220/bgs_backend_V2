package de.bgs.primary

import de.bgs.core.BoardGameService
import de.bgs.core.BoardGameShortDto
import de.bgs.core.FilterRequest
import de.bgs.secondary.database.BoardGameItem
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/boardgame")
class BoardGameController(private val boardGameService: BoardGameService) {

    @GetMapping(produces = ["application/json"])
    @Operation(summary = "Get board games in a page")
    fun getBoardGame(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int,
    ): BoardGamePageDto {
        val pageRequest = PageRequest.of(pageNumber, pageSize)
        val boardGames: Page<BoardGameItem> = boardGameService.getBoardGameItems(pageRequest)
        return BoardGamePageDto(boardGames.content, pageNumber, pageSize, boardGames.totalElements)
    }

    @PostMapping(consumes = ["application/json"], produces = ["application/json"])
    @Operation(summary = "Filter board games")
    fun filterBoardGame(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int,
        @RequestBody(required = false) filterRequest: FilterRequest = FilterRequest(),
    ): BoardGamePageDto {
        val pageRequest = PageRequest.of(pageNumber, pageSize)
        val boardGames: Page<BoardGameItem> = boardGameService.filterBoardGameItems(filterRequest, pageRequest)
        return BoardGamePageDto(boardGames.content, pageNumber, pageSize, boardGames.totalElements)
    }

    @PostMapping(path = ["/short"], consumes = ["application/json"], produces = ["application/json"])
    @Operation(summary = "Filter board games, returns shortened version")
    fun filterBoardGameShort(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int,
        @RequestBody(required = false) filterRequest: FilterRequest = FilterRequest(),
    ): BoardGameShortPageDto {
        val pageRequest = PageRequest.of(pageNumber, pageSize)
        val boardGames: Page<BoardGameItem> = boardGameService.filterBoardGameItems(filterRequest, pageRequest)
        return BoardGameShortPageDto(
            BoardGameShortDto.from(boardGames.content),
            pageNumber,
            pageSize,
            boardGames.totalElements
        )
    }
}
