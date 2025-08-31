package de.bgs.primary

import de.bgs.core.BoardGameFilterField
import de.bgs.core.BoardGameService
import de.bgs.core.BoardGameShortDto
import de.bgs.core.FilterRequest
import de.bgs.core.UpdateService
import de.bgs.primary.dto.BoardGamePageDto
import de.bgs.primary.dto.BoardGameShortPageDto
import de.bgs.secondary.database.BoardGameItem
import de.bgs.secondary.database.BoardGameItem_
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/boardgame")
class BoardGameController(private val boardGameService: BoardGameService, private val updateService: UpdateService) {

    companion object {
        private val DEFAULT_SORT_COLUMN = Sort.by(BoardGameItem_.BGG_ID)
    }

    @GetMapping(produces = ["application/json"])
    @Operation(summary = "Get board games in a page")
    fun getBoardGame(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(
            required = false,
            defaultValue = "10"
        ) @Schema(description = "If flag loadMetaData is set to true, pageSize is limited to 20.") pageSize: Int,
        @RequestParam(required = false) sortColumn: BoardGameFilterField?,
        @RequestParam(required = false, defaultValue = "true") sortAscending: Boolean,
        @RequestParam(required = false, defaultValue = "false") loadMetaDataIfMissing: Boolean,
    ): BoardGamePageDto {
        val pageRequest = getPageRequest(sortColumn, sortAscending, pageNumber, pageSize, loadMetaDataIfMissing)
        val boardGames: Page<BoardGameItem> = boardGameService.getBoardGameItems(pageRequest, loadMetaDataIfMissing)
        return BoardGamePageDto(boardGames.content, pageNumber, pageSize, boardGames.totalElements)
    }

    @GetMapping(path = ["/update"])
    @Operation(summary = "Manually trigger update of board game data")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateBoardGameData() {
        updateService.triggerUpdateDatabase()
    }

    @PostMapping(consumes = ["application/json"], produces = ["application/json"])
    @Operation(summary = "Filter board games")
    fun filterBoardGame(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(
            required = false,
            defaultValue = "10"
        ) @Schema(description = "If flag loadMetaData is set to true, pageSize is limited to 20.") pageSize: Int,
        @RequestParam(required = false) sortColumn: BoardGameFilterField?,
        @RequestParam(required = false, defaultValue = "true") sortAscending: Boolean,
        @RequestParam(required = false, defaultValue = "false") loadMetaDataIfMissing: Boolean,
        @RequestBody(required = false) filterRequest: FilterRequest = FilterRequest(),
    ): BoardGamePageDto {
        val pageRequest = getPageRequest(sortColumn, sortAscending, pageNumber, pageSize, loadMetaDataIfMissing)
        val boardGames: Page<BoardGameItem> =
            boardGameService.filterBoardGameItems(filterRequest, pageRequest, loadMetaDataIfMissing)
        return BoardGamePageDto(boardGames.content, pageNumber, pageSize, boardGames.totalElements)
    }

    @PostMapping(path = ["/short"], consumes = ["application/json"], produces = ["application/json"])
    @Operation(summary = "Filter board games, returns shortened version")
    fun filterBoardGameShort(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(
            required = false,
            defaultValue = "10"
        ) @Schema(description = "If flag loadMetaData is set to true, pageSize is limited to 20.") pageSize: Int,
        @RequestParam(required = false) sortColumn: BoardGameFilterField?,
        @RequestParam(required = false, defaultValue = "true") sortAscending: Boolean,
        @RequestParam(required = false, defaultValue = "false") loadMetaDataIfMissing: Boolean,
        @RequestBody(required = false) filterRequest: FilterRequest = FilterRequest(),
    ): BoardGameShortPageDto {
        val pageRequest = getPageRequest(sortColumn, sortAscending, pageNumber, pageSize, loadMetaDataIfMissing)
        val boardGames: Page<BoardGameItem> =
            boardGameService.filterBoardGameItems(filterRequest, pageRequest, loadMetaDataIfMissing)
        return BoardGameShortPageDto(
            BoardGameShortDto.from(boardGames.content),
            pageNumber,
            pageSize,
            boardGames.totalElements
        )
    }

    private fun getPageRequest(
        sortColumn: BoardGameFilterField?,
        sortAscending: Boolean,
        pageNumber: Int,
        pageSize: Int,
        loadMetaDataIfMissing: Boolean
    ): PageRequest {
        if (loadMetaDataIfMissing && pageSize > 20) throw IllegalArgumentException("Cannot load metadata for more than 20 items at a time")
        val sort: Sort = sortColumn?.let { Sort.by(sortColumn.searchField.name) } ?: DEFAULT_SORT_COLUMN
        sortAscending.takeIf { !it }?.let { sort.descending() }
        val pageRequest = PageRequest.of(pageNumber, pageSize, sort)
        return pageRequest
    }
}
