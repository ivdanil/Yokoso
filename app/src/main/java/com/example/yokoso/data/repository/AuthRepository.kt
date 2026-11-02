package com.example.yokoso.data.repository

import com.example.yokoso.data.network.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthRepository {
    private val auth = SupabaseClient.client.auth

    suspend fun signUp(email: String, password: String, name: String) {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
            data = buildJsonObject { put("name", name) }
        }
    }

    suspend fun signIn(email: String, password: String) {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    fun currentUserId(): String? = auth.currentUserOrNull()?.id

    suspend fun signOut() = auth.signOut()
}