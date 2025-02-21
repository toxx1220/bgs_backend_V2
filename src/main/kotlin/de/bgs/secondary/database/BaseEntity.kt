package de.bgs.secondary.database

import jakarta.persistence.*

@MappedSuperclass
abstract class BaseEntity {
    abstract var technicalId: Long?
    abstract var bggId: Long
    abstract var name: String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as BaseEntity

        return technicalId == other.technicalId
    }

    override fun hashCode(): Int = javaClass.hashCode()
}

@Entity
class GameFamily(
    @Id
    @GeneratedValue
    override var technicalId: Long? = null,

    @Column(unique = true)
    override var bggId: Long,

    override var name: String = "",

    ) : BaseEntity()

@Entity
class GameType(
    @Id
    @GeneratedValue
    override var technicalId: Long? = null,

    @Column(unique = true)
    override var bggId: Long,

    override var name: String = "",

    ) : BaseEntity()

@Entity
class Person(
    @Id
    @GeneratedValue
    override var technicalId: Long? = null,

    @Column(unique = true)
    override var bggId: Long,

    override var name: String = "",

    ) : BaseEntity()

@Entity
class GameCategory(
    @Id
    @GeneratedValue
    override var technicalId: Long? = null,

    @Column(unique = true)
    override var bggId: Long,

    override var name: String = "",

    ) : BaseEntity()

@Entity
class Mechanic(
    @Id
    @GeneratedValue
    override var technicalId: Long? = null,

    @Column(unique = true)
    override var bggId: Long,

    override var name: String = "",

    ) : BaseEntity()

@Entity
class Publisher(
    @Id
    @GeneratedValue
    override var technicalId: Long? = null,

    @Column(unique = true)
    override var bggId: Long,

    override var name: String = "",

    ) : BaseEntity()
