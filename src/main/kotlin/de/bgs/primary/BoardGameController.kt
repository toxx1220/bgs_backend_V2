package de.bgs.primary

import de.bgs.core.BoardGameService
import de.bgs.secondary.database.BoardGameItem
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class BoardGameController(private val boardGameService: BoardGameService) {

    @GetMapping
    fun getHello(): String {
        return "Hello. It is ${LocalDateTime.now()}"
    }

    @GetMapping("/boardgame")
    fun getBoardGame(): List<BoardGameItem> {
        return boardGameService.getBoardGameItems()
    }

    @PostMapping("/boardgame") // TODO: just for debug. remove me
    fun saveBoardGame(@RequestBody boardGameItem: BoardGameItem): BoardGameItem {
        return boardGameService.saveBoardGame(boardGameItem)
    }
}