package com.xomato.data.models

import kotlinx.serialization.Serializable

@Serializable
data class PaginateWrapper<T>(
    val results: T,
    val pageSize: Int,
    val page: Int,
)


