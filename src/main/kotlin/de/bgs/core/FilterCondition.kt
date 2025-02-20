package de.bgs.core

class FilterCondition(
    val field: BoardGameField,
    val operator: Operator,
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