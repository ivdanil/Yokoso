package com.example.yokoso.data.repository

import com.example.yokoso.data.model.FavoriteInsert
import com.example.yokoso.data.model.FavoriteRow
import com.example.yokoso.data.model.FavoriteWithPlace
import com.example.yokoso.data.model.PlaceWithDetails
import com.example.yokoso.data.network.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.postgrest

class FavoritesRepository {

    suspend fun getFavoritePlaces(): List<PlaceWithDetails> {
        val uid = userId ?: return emptyList()
        return SupabaseClient.client.postgrest.from("favorites")
            .select(columns = Columns.raw("places(id, name, description, photo_url, categories(name), cities(name))")) {
                filter { eq("user_id", uid) }
            }
            .decodeList<FavoriteWithPlace>()
            .map { it.places }
    }
    private val userId get() = SupabaseClient.client.auth.currentUserOrNull()?.id

    suspend fun getFavoriteIds(): Set<String> {
        val uid = userId ?: return emptySet()
        return SupabaseClient.client.postgrest.from("favorites")
            .select { filter { eq("user_id", uid) } }
            .decodeList<FavoriteRow>()
            .map { it.place_id }
            .toSet()
    }

    suspend fun add(placeId: String) {
        val uid = userId ?: return
        SupabaseClient.client.postgrest.from("favorites").insert(FavoriteInsert(uid, placeId))
    }

    suspend fun remove(placeId: String) {
        val uid = userId ?: return
        SupabaseClient.client.postgrest.from("favorites").delete {
            filter {
                eq("user_id", uid)
                eq("place_id", placeId)
            }
        }
    }
}