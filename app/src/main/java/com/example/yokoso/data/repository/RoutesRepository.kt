package com.example.yokoso.data.repository

import com.example.yokoso.data.model.PlaceWithDetails
import com.example.yokoso.data.model.RouteInsert
import com.example.yokoso.data.model.RoutePlaceInsert
import com.example.yokoso.data.model.RoutePlaceWithPlace
import com.example.yokoso.data.model.RouteRow
import com.example.yokoso.data.network.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

class RoutesRepository {
    private val db get() = SupabaseClient.client.postgrest
    private val userId get() = SupabaseClient.client.auth.currentUserOrNull()?.id

    private suspend fun getOrCreateRouteId(): String? {
        val uid = userId ?: return null
        val existing = db.from("routes")
            .select { filter { eq("user_id", uid) } }
            .decodeList<RouteRow>()
            .firstOrNull()
        if (existing != null) return existing.id

        return db.from("routes")
            .insert(RouteInsert(uid, "Мой маршрут")) { select() }
            .decodeSingle<RouteRow>()
            .id
    }

    suspend fun getRoutePlaces(): List<PlaceWithDetails> {
        val routeId = getOrCreateRouteId() ?: return emptyList()
        return db.from("route_places")
            .select(
                columns = Columns.raw(
                    "order_num, places(id, name, description, photo_url, categories(name), cities(name))"
                )
            ) {
                filter { eq("route_id", routeId) }
                order("order_num", Order.ASCENDING)
            }
            .decodeList<RoutePlaceWithPlace>()
            .map { it.places }
    }

    suspend fun addPlace(placeId: String) {
        val routeId = getOrCreateRouteId() ?: return
        val current = getRoutePlaces()
        if (current.any { it.id == placeId }) return
        db.from("route_places").insert(RoutePlaceInsert(routeId, placeId, current.size + 1))
    }

    suspend fun removePlace(placeId: String) {
        val routeId = getOrCreateRouteId() ?: return
        db.from("route_places").delete {
            filter {
                eq("route_id", routeId)
                eq("place_id", placeId)
            }
        }
    }
}