package com.example.yokoso.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RouteRow(val id: String, val title: String? = null)

@Serializable
data class RouteInsert(val user_id: String, val title: String)

@Serializable
data class RoutePlaceInsert(val route_id: String, val place_id: String, val order_num: Int)

@Serializable
data class RoutePlaceWithPlace(val order_num: Int? = null, val places: PlaceWithDetails)