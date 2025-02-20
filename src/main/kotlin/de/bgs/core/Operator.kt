package de.bgs.core

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Expression
import jakarta.persistence.criteria.Predicate

enum class Operator(val supportedTypes: Any) {
    EQUALS(Comparable::class),
    GREATER_THAN(Comparable::class),
    GREATER_THAN_OR_EQUALS(Comparable::class),
    LESS_THAN(Comparable::class),
    LESS_THAN_OR_EQUALS(Comparable::class),
    LIKE(String::class),
    IS_TRUE(Boolean::class),
    IS_FALSE(Boolean::class);

    @Suppress("UNCHECKED_CAST")
    fun getPredicate(
        criteriaBuilder: CriteriaBuilder,
        expression: Expression<*>,
        filterValue: Any?
    ): Predicate {
        if (this in setOf(IS_TRUE, IS_FALSE)) {
            require(filterValue == null) {
                "Filter value must be null for operator $this."
            }
        } else {
            require(filterValue != null) {
                "Filter value must not be null for operator $this."
            }
            val filterValueClass = filterValue::class
            val supportedClasses = when (supportedTypes) {
                is kotlin.reflect.KClass<*> -> setOf(supportedTypes)
                is Set<*> -> supportedTypes.filterIsInstance<kotlin.reflect.KClass<*>>().toSet()
                else -> emptySet()
            }
            require(supportedClasses.any { it.java.isAssignableFrom(filterValueClass.java) }) {
                "Filter value type ${filterValue.javaClass} not supported for operator $this."
            }
        }
        return when (this) {
            EQUALS -> criteriaBuilder.equal(expression, filterValue)
            GREATER_THAN -> criteriaBuilder.greaterThan(
                expression as Expression<Comparable<Any>>,
                filterValue as Comparable<Any>
            )

            GREATER_THAN_OR_EQUALS -> criteriaBuilder.greaterThanOrEqualTo(
                expression as Expression<Comparable<Any>>,
                filterValue as Comparable<Any>
            )

            LESS_THAN -> criteriaBuilder.lessThan(
                expression as Expression<Comparable<Any>>,
                filterValue as Comparable<Any>
            )

            LESS_THAN_OR_EQUALS -> criteriaBuilder.lessThanOrEqualTo(
                expression as Expression<Comparable<Any>>,
                filterValue as Comparable<Any>
            )

            LIKE -> criteriaBuilder.like(expression as Expression<String>, filterValue as String)
            IS_TRUE -> criteriaBuilder.isTrue(expression as Expression<Boolean>)
            IS_FALSE -> criteriaBuilder.isFalse(expression as Expression<Boolean>)
        }
    }
}