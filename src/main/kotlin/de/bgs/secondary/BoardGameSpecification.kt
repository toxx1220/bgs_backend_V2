package de.bgs.secondary

import de.bgs.core.FilterCondition
import de.bgs.core.Operator
import de.bgs.secondary.database.BoardGameItem
import jakarta.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification

class BoardGameSpecification(val filterConditions: Set<FilterCondition>) : Specification<BoardGameItem> {

    override fun toPredicate(
        root: Root<BoardGameItem>,
        query: CriteriaQuery<*>?,
        criteriaBuilder: CriteriaBuilder
    ): Predicate {
        val predicates = ArrayList<Predicate>()
        val expression: Expression<*> = root
        for (condition in filterConditions) {
            var from: From<*, *> = root
            // Perform joins for all but the last attribute
            val pathList = condition.field.getJoinPath()
            for (attribute in pathList) {
                from = from.join<Any, Any>(attribute.name, JoinType.INNER)
            }
            // Get final attribute
            val finalAttribute = condition.field.getField()
            val path = from.get<Any>(finalAttribute.name)

            val predicate = when (condition.operator) {
                Operator.EQUALS -> criteriaBuilder.equal(path, condition.filterValue)
                Operator.GREATER_THAN -> criteriaBuilder.greaterThan(
                    path as Expression<Comparable<Any>>,
                    condition.filterValue as Comparable<Any>
                )

                Operator.GREATER_THAN_OR_EQUALS -> criteriaBuilder.greaterThanOrEqualTo(
                    path as Expression<Comparable<Any>>,
                    condition.filterValue as Comparable<Any>
                )

                Operator.LESS_THAN -> criteriaBuilder.lessThan(
                    path as Expression<Comparable<Any>>,
                    condition.filterValue as Comparable<Any>
                )

                Operator.LESS_THAN_OR_EQUALS -> criteriaBuilder.lessThanOrEqualTo(
                    path as Expression<Comparable<Any>>,
                    condition.filterValue as Comparable<Any>
                )

                Operator.LIKE -> criteriaBuilder.like(path as Expression<String>, "%${condition.filterValue}%")
                Operator.IS_TRUE -> criteriaBuilder.isTrue(path as Expression<Boolean>)
                Operator.IS_FALSE -> criteriaBuilder.isFalse(path as Expression<Boolean>)
            }

            predicates.add(predicate)
        }
        return criteriaBuilder.and(*predicates.toTypedArray())
    }
}