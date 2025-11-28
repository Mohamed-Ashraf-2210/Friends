package com.trrycaar.friends.data.remote.dto.posts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    @SerialName("hasNext")
    val hasNext: Boolean,
    @SerialName("hasPrev")
    val hasPrev: Boolean,
    @SerialName("limit")
    val limit: Int,
    @SerialName("page")
    val page: Int,
    @SerialName("total")
    val total: Int,
    @SerialName("totalPages")
    val totalPages: Int
)