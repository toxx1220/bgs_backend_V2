package de.bgs.primary

import de.bgs.core.BoardGameService
import de.bgs.secondary.database.BoardGameItem
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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
    ): List<BoardGameItem> {
        val pageRequest = PageRequest.of(pageNumber, pageSize)
        return boardGameService.getBoardGameItems(pageRequest).content
    }
}