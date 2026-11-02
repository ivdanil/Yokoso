package com.example.yokoso.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteInsert(val user_id: String, val place_id: String)

@Serializable
data class FavoriteRow(val place_id: String)

@Serializable
data class FavoriteWithPlace(val places: PlaceWithDetails)