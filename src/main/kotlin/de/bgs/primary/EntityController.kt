package de.bgs.primary

import de.bgs.secondary.*
import de.bgs.secondary.database.GameCategory
import de.bgs.secondary.database.GameFamily
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
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
    ): BaseEntityDto<GameFamily> {
        val families = gameFamilyJpaRepo.findAll(PageRequest.of(pageNumber, pageSize))
        return BaseEntityDto(families.content, pageNumber, pageSize, families.totalElements)
    }

    @GetMapping("/gameCategory", consumes = ["application/json"], produces = ["application/json"])
    fun getGameCategory(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int
    ): BaseEntityDto<GameCategory> {
        val categories = categoryJpaRepo.findAll(PageRequest.of(pageNumber, pageSize))
        return BaseEntityDto(categories.content, pageNumber, pageSize, categories.totalElements)
    }

    @GetMapping("/gameMechanic", consumes = ["application/json"], produces = ["application/json"])
    fun getGameMechanic(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int
    ) {
        val mechanics = mechanicJpaRepo.findAll(PageRequest.of(pageNumber, pageSize))
        ResponseEntity.ok(BaseEntityDto(mechanics.content, pageNumber, pageSize, mechanics.totalElements))
    }

    @GetMapping("/gameType", consumes = ["application/json"], produces = ["application/json"])
    fun getGameType(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int
    ) {
        val gameTypes = gameTypeJpaRepo.findAll(PageRequest.of(pageNumber, pageSize))
        ResponseEntity.ok(BaseEntityDto(gameTypes.content, pageNumber, pageSize, gameTypes.totalElements))
    }

    @GetMapping("/person", consumes = ["application/json"], produces = ["application/json"])
    fun getPerson(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int
    ) {
        val persons = personJpaRepo.findAll(PageRequest.of(pageNumber, pageSize))
        ResponseEntity.ok(BaseEntityDto(persons.content, pageNumber, pageSize, persons.totalElements))
    }

    @GetMapping("/publisher", consumes = ["application/json"], produces = ["application/json"])
    fun getPublisher(
        @RequestParam(required = false, defaultValue = "0") pageNumber: Int,
        @RequestParam(required = false, defaultValue = "10") pageSize: Int
    ) {
        val publishers = publisherJpaRepo.findAll(PageRequest.of(pageNumber, pageSize))
        ResponseEntity.ok(BaseEntityDto(publishers.content, pageNumber, pageSize, publishers.totalElements))
    }
}