package de.bgs.secondary

import de.bgs.core.FilterRequest
import de.bgs.secondary.database.BoardGameItem
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.criteria.*
import org.springframework.data.jpa.domain.Specification

class BoardGameSpecification(private val filterRequest: FilterRequest) : Specification<BoardGameItem> {
    private val logger = KotlinLogging.logger { }

    override fun toPredicate(
        root: Root<BoardGameItem>,
        query: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ): Predicate {
        val predicates = ArrayList<Predicate>()
        for (condition in filterRequest.filterConditions) {
            logger.info { "Processing filter condition: fieldName: ${condition.field.name}, operator: ${condition.operator.name}, filterValue: ${condition.filterValue}" }

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
