package com.example.yokoso.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ReviewWithAuthor(
    val id: String,
    val rating: Int? = null,
    val review_text: String? = null,
    val profiles: NamedRef? = null
)

@Serializable
data class ReviewInsert(
    val user_id: String,
    val place_id: String,
    val rating: Int,
    val review_text: String
)