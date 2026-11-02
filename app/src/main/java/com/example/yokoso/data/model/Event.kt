package com.example.yokoso.data.model

import kotlinx.serialization.Serializable

@Serializable
data class EventWithCity(
    val id: String,
    val name: String,
    val event_date: String? = null,
    val cities: NamedRef? = null
)