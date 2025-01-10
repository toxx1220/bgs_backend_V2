package de.bgs.secondary.database

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import org.hibernate.Hibernate
import org.springframework.data.jpa.domain.AbstractPersistable_.id

@Entity
class GameFamily(
    @Id
    @GeneratedValue
    var technicalId: Long? = null,

    @Column(unique = true)
    val gameFamilyId: Long,

    var name: String,

    @ManyToMany
    var boardGame: MutableSet<BoardGameItem> = mutableSetOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as GameFamily

        return technicalId == other.technicalId
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
/*
class GameFamily {
    @Id
    @Column(nullable = false)
    val id: BigDecimal,
    var name: String = ""

    @ManyToMany
    var boardGame: MutableSet<BoardGameItem> = mutableSetOf()

}*/
