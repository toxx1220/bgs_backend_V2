package de.bgs.core

import de.bgs.core.Operator.IS_FALSE
import de.bgs.core.Operator.IS_TRUE
import io.swagger.v3.oas.annotations.media.Schema
import kotlin.reflect.KClass

@Schema(description = "One filter condition for board game items.")
data class FilterCondition(
    @Schema(description = "Field to filter on.")
    val field: BoardGameField,
    @Schema(description = "Operator to use for filtering.")
    val operator: Operator,
    @Schema(description = "Value to filter for. Must be of the correct type for the operator.")
    val filterValue: Any?,
) {
    init {
        // Convert primitive fields to their wrapper classes
        val fieldType = field.searchField.javaType.let {
            if (it.isPrimitive) it.kotlin.javaObjectType else it
        }
        val isSupported = when (val types = operator.supportedTypes) {
            is KClass<*> -> types.java.isAssignableFrom(fieldType)
            is Set<*> -> types.any { (it as? KClass<*>)?.java?.isAssignableFrom(fieldType) == true }
            else -> false
        }
        require(isSupported) {
            "Operator ${operator.name} is not supported for field ${field.searchField.name}"
        }
        if (operator in setOf(IS_TRUE, IS_FALSE)) {
            require(filterValue == null) {
                "Filter value must be null for operator $this."
            }
        } else {
            require(filterValue != null) {
                "Filter value must not be null for operator $this."
            }
            val filterValueClass = filterValue::class
            val supportedClasses = when (operator.supportedTypes) {
                is KClass<*> -> setOf(operator.supportedTypes)
                is Set<*> -> operator.supportedTypes.filterIsInstance<KClass<*>>().toSet()
                else -> emptySet()
            }
            require(supportedClasses.any { it.java.isAssignableFrom(filterValueClass.java) }) {
                "Filter value type ${filterValue.javaClass} not supported for operator $this."
            }
        }
    }
}

@Schema(description = "List of filter conditions. Will be AND combined.")
data class FilterRequest(
    val filterConditions: Set<FilterCondition> = emptySet(),
)