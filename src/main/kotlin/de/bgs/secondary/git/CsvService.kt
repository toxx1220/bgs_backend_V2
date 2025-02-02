package de.bgs.secondary.git

import de.bgs.secondary.database.BoardGameItem
import de.bgs.secondary.database.GameFamily
import org.springframework.stereotype.Service
import java.io.File

@Service
class CsvService(private val repoRoot: File) {

    fun parseGameFamily(): List<GameFamily> {
        TODO()
    }

    fun parseBoardGame(): HashMap<Long, BoardGameItem> {
        TODO()
    }
}