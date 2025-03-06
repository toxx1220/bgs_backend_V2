package de.bgs.secondary

import de.bgs.secondary.database.*
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BoardGameJpaRepo : JpaSpecificationExecutor<BoardGameItem>, JpaRepository<BoardGameItem, Long> {
    @Modifying
    @Transactional
    @Query(
        value = "TRUNCATE TABLE board_game_item RESTART IDENTITY CASCADE",
        nativeQuery = true
    )
    fun truncateWithCascade()
}

@Repository
interface GameFamilyJpaRepo : JpaRepository<GameFamily, Long> {
    @Modifying
    @Query(
        value = "TRUNCATE TABLE game_family RESTART IDENTITY CASCADE",
        nativeQuery = true
    )
    fun truncateWithCascade()
}

@Repository
interface GameTypeJpaRepo : JpaRepository<GameType, Long> {
    @Modifying
    @Transactional
    @Query(
        value = "TRUNCATE TABLE game_type RESTART IDENTITY CASCADE",
        nativeQuery = true
    )
    fun truncateWithCascade()
}

@Repository
interface PersonJpaRepo : JpaRepository<Person, Long> {
    @Modifying
    @Transactional
    @Query(
        value = "TRUNCATE TABLE person RESTART IDENTITY CASCADE",
        nativeQuery = true
    )
    fun truncateWithCascade()
}

@Repository
interface CategoryJpaRepo : JpaRepository<GameCategory, Long> {
    @Modifying
    @Transactional
    @Query(
        value = "TRUNCATE TABLE game_category RESTART IDENTITY CASCADE",
        nativeQuery = true
    )
    fun truncateWithCascade()
}

@Repository
interface MechanicJpaRepo : JpaRepository<GameMechanic, Long> {
    @Modifying
    @Transactional
    @Query(
        value = "TRUNCATE TABLE game_mechanic RESTART IDENTITY CASCADE",
        nativeQuery = true
    )
    fun truncateWithCascade()
}

@Repository
interface PublisherJpaRepo : JpaRepository<Publisher, Long> {
    @Modifying
    @Transactional
    @Query(
        value = "TRUNCATE TABLE publisher RESTART IDENTITY CASCADE",
        nativeQuery = true
    )
    fun truncateWithCascade()
}