package com.example.yokoso.data.repository

import com.example.yokoso.data.model.ProfileRow
import com.example.yokoso.data.network.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest

class ProfileRepository {
    private val user get() = SupabaseClient.client.auth.currentUserOrNull()

    fun getEmail(): String = user?.email ?: ""

    suspend fun getName(): String {
        val uid = user?.id ?: return ""
        return SupabaseClient.client.postgrest.from("profiles")
            .select { filter { eq("id", uid) } }
            .decodeSingle<ProfileRow>()
            .name
    }
}