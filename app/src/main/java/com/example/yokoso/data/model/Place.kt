package com.example.yokoso.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val id: String,
    val name: String,
    val description: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val photo_url: String? = null,
    val category_id: String? = null,
    val city_id: String? = null
)

@Serializable
data class PlaceWithDetails(
    val id: String,
    val name: String,
    val description: String? = null,
    val photo_url: String? = null,
    val categories: NamedRef? = null,
    val cities: NamedRef? = null
)

@Serializable
data class NamedRef(val name: String)