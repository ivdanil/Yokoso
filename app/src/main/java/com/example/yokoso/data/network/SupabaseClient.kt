package com.example.yokoso.data.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.auth.Auth
import kotlin.time.Duration.Companion.seconds

object SupabaseClient {
    val client = createSupabaseClient(
        supabaseUrl = "https://fhjxugwjslgrrgycmvyg.supabase.co",
        supabaseKey = "sb_publishable_2oriQ29SIEkfeEtppjJctg_jBnOo7KT"
    ) {
        install(Postgrest)
        install(Auth)

        requestTimeout = 30.seconds
    }
}