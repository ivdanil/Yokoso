package com.example.yokoso.data.repository

import com.example.yokoso.data.model.ReviewInsert
import com.example.yokoso.data.model.ReviewWithAuthor
import com.example.yokoso.data.network.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

class ReviewsRepository {

    suspend fun getReviews(placeId: String): List<ReviewWithAuthor> {
        return SupabaseClient.client.postgrest
            .from("reviews")
            .select(columns = Columns.raw("id, rating, review_text, profiles(name)")) {
                filter { eq("place_id", placeId) }
            }
            .decodeList<ReviewWithAuthor>()
    }

    // добавляет отзыв текущего пользователя
    suspend fun addReview(placeId: String, rating: Int, text: String) {
        val uid = SupabaseClient.client.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("Нет активной сессии")
        SupabaseClient.client.postgrest
            .from("reviews")
            .insert(ReviewInsert(uid, placeId, rating, text))
    }
}