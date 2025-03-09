package de.bgs.core

import de.bgs.core.Operator.IS_FALSE
import de.bgs.core.Operator.IS_TRUE
import io.swagger.v3.oas.annotations.media.Schema
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

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
        val fieldKClass = field.searchField.javaType.kotlin

        // Convert operator.supportedTypes to a Set of KClass
        val supportedKClasses = when (val types = operator.supportedTypes) {
            is KClass<*> -> setOf(types)
            is Set<*> -> types.filterIsInstance<KClass<*>>().toSet()
            else -> emptySet()
        }

        require(supportedKClasses.any { it.isSuperclassOf(fieldKClass) }) {
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

                // Get the Kotlin KClass for the filter value
                val valueKClass = filterValue::class

                // Check compatibility using Kotlin's type system
                require(supportedKClasses.any { it.isSuperclassOf(valueKClass) }) {
                    "Invalid filter type ${valueKClass.simpleName} for $operator"
                }
            }
        }
    }
}

@Schema(description = "List of filter conditions. Will be AND combined.")
data class FilterRequest(
    val filterConditions: Set<FilterCondition> = emptySet(),
)