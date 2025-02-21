package de.bgs.secondary

import de.bgs.secondary.database.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BoardGameJpaRepo : JpaSpecificationExecutor<BoardGameItem>, JpaRepository<BoardGameItem, Long> {
    @Modifying
    @Query(
        value = "TRUNCATE TABLE board_game_item RESTART IDENTITY CASCADE",
        nativeQuery = true
    )
    fun truncateWithCascade()
}

@Repository
interface GameFamilyJpaRepo : JpaRepository<GameFamily, Long>

@Repository
interface GameTypeJpaRepo : JpaRepository<GameType, Long>

@Repository
interface PersonJpaRepo : JpaRepository<Person, Long>

@Repository
interface CategoryJpaRepo : JpaRepository<GameCategory, Long>

@Repository
interface MechanicJpaRepo : JpaRepository<GameMechanic, Long>

@Repository
interface PublisherJpaRepo : JpaRepository<Publisher, Long>