package de.bgs.core

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Predicate

enum class Operator(val supportedTypes: Any) {
    EQUALS(Comparable::class),
    GREATER_THAN(Comparable::class),
    GREATER_THAN_OR_EQUALS(Comparable::class),
    LESS_THAN(Comparable::class),
    LESS_THAN_OR_EQUALS(Comparable::class),
    LIKE(setOf(String::class)),
    IS_TRUE(setOf(Boolean::class)),
    IS_FALSE(setOf(Boolean::class));

    @Suppress("UNCHECKED_CAST")
    fun getPredicate(
        criteriaBuilder: CriteriaBuilder,
        expression: jakarta.persistence.criteria.Expression<*>,
        filterValue: Any
    ): Predicate {
        require(filterValue.javaClass.isInstance(supportedTypes)) {
            "Filter value type ${filterValue.javaClass} is not supported for operator $this"
        }
        require(expression.javaClass.isInstance(supportedTypes)) {
            "Expression type ${expression.javaClass} is not supported for operator $this"
        }

        return when (this) {
            EQUALS -> criteriaBuilder.equal(expression, filterValue)
            GREATER_THAN -> criteriaBuilder.greaterThan(
                expression as jakarta.persistence.criteria.Expression<Comparable<Any>>,
                filterValue as Comparable<Any>
            )

            GREATER_THAN_OR_EQUALS -> criteriaBuilder.greaterThanOrEqualTo(
                expression as jakarta.persistence.criteria.Expression<Comparable<Any>>,
                filterValue as Comparable<Any>
            )

            LESS_THAN -> criteriaBuilder.lessThan(
                expression as jakarta.persistence.criteria.Expression<Comparable<Any>>,
                filterValue as Comparable<Any>
            )

            LESS_THAN_OR_EQUALS -> criteriaBuilder.lessThanOrEqualTo(
                expression as jakarta.persistence.criteria.Expression<Comparable<Any>>,
                filterValue as Comparable<Any>
            )

            LIKE -> criteriaBuilder.like(
                expression as jakarta.persistence.criteria.Expression<String>,
                filterValue as String
            )

            IS_TRUE -> criteriaBuilder.isTrue(expression as jakarta.persistence.criteria.Expression<Boolean>)
            IS_FALSE -> criteriaBuilder.isFalse(expression as jakarta.persistence.criteria.Expression<Boolean>)
        }
    }
}
