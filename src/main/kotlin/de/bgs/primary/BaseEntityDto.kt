package de.bgs.primary

import de.bgs.secondary.database.BaseEntity

data class BaseEntityDto<T : BaseEntity>(
    val content: List<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Long
)