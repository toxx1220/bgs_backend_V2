package de.bgs.core

enum class Operator(val supportedTypes: Set<Any>) {
    EQUALS(setOf(String::class, Number::class, Boolean::class)),
    GREATER_THAN(setOf(Number::class)),
    GREATER_THAN_OR_EQUALS(setOf(Number::class)),
    LESS_THAN(setOf(Number::class)),
    LESS_THAN_OR_EQUALS(setOf(Number::class)),
    LIKE(setOf(String::class)),
    IS_TRUE(setOf(Boolean::class)),
    IS_FALSE(setOf(Boolean::class)),
}
