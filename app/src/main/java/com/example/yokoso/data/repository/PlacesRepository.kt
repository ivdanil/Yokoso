package com.example.yokoso.data.repository

import com.example.yokoso.data.model.PlaceWithDetails
import com.example.yokoso.data.network.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

class PlacesRepository {
    suspend fun getPlaces(): List<PlaceWithDetails> {
        return SupabaseClient.client.postgrest
            .from("places")
            .select(columns = Columns.raw("*, categories(name), cities(name)"))
            .decodeList<PlaceWithDetails>()
    }

    suspend fun getPlace(id: String): PlaceWithDetails {
        return SupabaseClient.client.postgrest
            .from("places")
            .select(columns = Columns.raw("*, categories(name), cities(name)")) {
                filter { eq("id", id) }
            }
            .decodeSingle<PlaceWithDetails>()
    }
}