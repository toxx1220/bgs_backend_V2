package de.bgs.core

class FilterCondition(
    val field: BoardGameField,
    val operator: Operator,
    val filterValue: Any,
) {
    init {
        if (field.getField().javaType !in operator.supportedTypes) {
            throw IllegalArgumentException("Operator ${operator.name} is not supported for field ${field.getFieldName()}")
        }
    }
}