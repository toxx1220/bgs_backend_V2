package de.bgs.secondary.database

import jakarta.persistence.MappedSuperclass

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
