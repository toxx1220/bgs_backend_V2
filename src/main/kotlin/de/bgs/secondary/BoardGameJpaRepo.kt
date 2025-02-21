package de.bgs.secondary

import de.bgs.secondary.database.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BoardGameJpaRepo : JpaSpecificationExecutor<BoardGameItem>, JpaRepository<BoardGameItem, Long> {
    fun findByBggId(bggId: Long): Optional<BoardGameItem>

    @Modifying
    @Query(
        value = "TRUNCATE TABLE board_game_item RESTART IDENTITY CASCADE",
        nativeQuery = true
    )
    fun truncateWithCascade()
}

@Repository
interface GameFamilyJpaRepo : JpaRepository<GameFamily, Long> {
    fun findByBggId(bggId: Long): Optional<GameFamily>
    fun findByBggIdIn(bggIds: List<Long>): MutableSet<GameFamily>
}

@Repository
interface GameTypeJpaRepo : JpaRepository<GameType, Long> {
    fun findByBggId(bggId: Long): Optional<GameType>
    fun findByBggIdIn(bggIds: List<Long>): MutableSet<GameType>
}

@Repository
interface PersonJpaRepo : JpaRepository<Person, Long> {
    fun findByBggId(bggId: Long): Optional<Person>
    fun findByBggIdIn(bggIds: List<Long>): MutableSet<Person>
}

@Repository
interface CategoryJpaRepo : JpaRepository<GameCategory, Long> {
    fun findByBggId(bggId: Long): Optional<GameCategory>
    fun findByBggIdIn(bggIds: List<Long>): MutableSet<GameCategory>
}

@Repository
interface MechanicJpaRepo : JpaRepository<Mechanic, Long> {
    fun findByBggId(bggId: Long): Optional<Mechanic>
    fun findByBggIdIn(bggIds: List<Long>): MutableSet<Mechanic>
}

@Repository
interface PublisherJpaRepo : JpaRepository<Publisher, Long> {
    fun findByBggId(bggId: Long): Optional<Publisher>
    fun findByBggIdIn(bggIds: List<Long>): MutableSet<Publisher>
}