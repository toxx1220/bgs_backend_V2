package de.bgs.core

import de.bgs.core.Operator.IS_FALSE
import de.bgs.core.Operator.IS_TRUE
import io.swagger.v3.oas.annotations.media.Schema
import kotlin.reflect.KClass

@Schema(description = "One filter condition for board game items.")
data class FilterCondition(
    @Schema(description = "Field to filter on.")
    val field: BoardGameFilterField,
    @Schema(description = "Operator to use for filtering.")
    val operator: Operator,
    @Schema(description = "Value to filter for. Must be of the correct type for the operator.")
    val filterValue: Any?,
) {
    init {
        val fieldType = field.searchField.javaType.let {
            if (it.isPrimitive) it.kotlin.javaObjectType else it
        }

        val supportedClasses = when (val types = operator.supportedTypes) {
            is KClass<*> -> setOf(types.java)
            is Set<*> -> types.filterIsInstance<KClass<*>>().map { it.java }.toSet()
            else -> emptySet()
        }

        require(supportedClasses.any { it.isAssignableFrom(fieldType) }) {
            "Operator ${operator.name} unsupported for field ${field.searchField.name}"
        }

        when (operator) {
            IS_TRUE, IS_FALSE -> require(filterValue == null) {
                "Filter value must be null for $operator"
            }

            else -> {
                require(filterValue != null) {
                    "Filter value required for $operator"
                }
                val valueClass = filterValue::class.java
                require(supportedClasses.any { it.isAssignableFrom(valueClass) }) {
                    "Invalid filter type ${valueClass.name} for $operator"
                }
            }
        }
    }
}

@Schema(description = "List of filter conditions. Will be AND combined.")
data class FilterRequest(
    val filterConditions: Set<FilterCondition> = emptySet(),
)