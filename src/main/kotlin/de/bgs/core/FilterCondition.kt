package de.bgs.core

import io.swagger.v3.oas.annotations.media.Schema

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
        val fieldType = field.searchField.type.javaType
        val isSupported = when (val types = operator.supportedTypes) {
            is kotlin.reflect.KClass<*> ->
                types.java.isAssignableFrom(fieldType)

            is Set<*> ->
                types.any { (it as? kotlin.reflect.KClass<*>)?.java?.isAssignableFrom(fieldType) == true }

            else -> false
        }
        require(isSupported) {
            "Operator ${operator.name} is not supported for field ${field.searchField.name}"
        }
    }
}

@Schema(description = "List of filter conditions. Will be AND combined.")
data class FilterRequest(
    val filterConditions: Set<FilterCondition> = emptySet(),
)