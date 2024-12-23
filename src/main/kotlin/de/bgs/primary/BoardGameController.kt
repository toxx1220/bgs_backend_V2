package de.bgs.primary

import de.bgs.core.BoardGameService
import de.bgs.secondary.database.BoardGameItem
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/boardgame")
class BoardGameController(private val boardGameService: BoardGameService) {

    @GetMapping
    fun getBoardGame(): List<BoardGameItem> {
        return boardGameService.getBoardGameItems()
    }
}