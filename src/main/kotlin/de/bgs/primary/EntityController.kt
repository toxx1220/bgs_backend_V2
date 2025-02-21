package de.bgs.primary

import de.bgs.secondary.*
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class EntityController(
    private val gameFamilyJpaRepo: GameFamilyJpaRepo,
    private val categoryJpaRepo: CategoryJpaRepo,
    private val mechanicJpaRepo: MechanicJpaRepo,
    private val gameTypeJpaRepo: GameTypeJpaRepo,
    private val personJpaRepo: PersonJpaRepo,
    private val publisherJpaRepo: PublisherJpaRepo,
) {

    @GetMapping(
        "/gameFamily", consumes = ["application/json"], produces = ["application/json"]
    )
    fun getGameFamily(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int
    ) = gameFamilyJpaRepo.findAll(PageRequest.of(pageNumber, pageSize))

    @GetMapping("/gameCategory", consumes = ["application/json"], produces = ["application/json"])
    fun getGameCategory(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int
    ) = categoryJpaRepo.findAll(PageRequest.of(pageNumber, pageSize))

    @GetMapping("/gameMechanic", consumes = ["application/json"], produces = ["application/json"])
    fun getGameMechanic(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int
    ) = mechanicJpaRepo.findAll(PageRequest.of(pageNumber, pageSize))

    @GetMapping("/gameType", consumes = ["application/json"], produces = ["application/json"])
    fun getGameType(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int
    ) = gameTypeJpaRepo.findAll(PageRequest.of(pageNumber, pageSize))

    @GetMapping("/person", consumes = ["application/json"], produces = ["application/json"])
    fun getPerson(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int
    ) = personJpaRepo.findAll(PageRequest.of(pageNumber, pageSize))

    @GetMapping("/publisher", consumes = ["application/json"], produces = ["application/json"])
    fun getPublisher(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int
    ) = publisherJpaRepo.findAll(PageRequest.of(pageNumber, pageSize))
}