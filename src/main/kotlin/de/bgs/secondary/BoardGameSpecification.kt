package de.bgs.secondary

import de.bgs.core.FilterCondition
import de.bgs.secondary.database.BoardGameItem
import jakarta.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification

class BoardGameSpecification(private val filterConditions: Set<FilterCondition>) : Specification<BoardGameItem> {

    override fun toPredicate(
        root: Root<BoardGameItem>,
        query: CriteriaQuery<*>?,
        criteriaBuilder: CriteriaBuilder
    ): Predicate {
        val predicates = ArrayList<Predicate>()
        for (condition in filterConditions) {

            val expression: Expression<Any> = if (condition.field.joinAttribute != null) {
                root.join(condition.field.joinAttribute, JoinType.INNER)
                    .get(condition.field.searchField.name)
            } else {
                root.get(condition.field.searchField.name)
            }

            predicates.add(condition.operator.getPredicate(criteriaBuilder, expression, condition.filterValue))
        }
        return criteriaBuilder.and(*predicates.toTypedArray())
    }
}
