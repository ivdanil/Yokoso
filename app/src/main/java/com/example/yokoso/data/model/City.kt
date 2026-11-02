package com.example.yokoso.data.model

import kotlinx.serialization.Serializable

@Serializable
data class City(val id: String, val name: String, val region: String? = null)