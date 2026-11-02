package com.example.yokoso.data.repository

import com.example.yokoso.data.model.EventWithCity
import com.example.yokoso.data.network.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

class EventsRepository {
    // события по возрастанию даты, вместе с названием города
    suspend fun getEvents(): List<EventWithCity> {
        return SupabaseClient.client.postgrest
            .from("events")
            .select(columns = Columns.raw("id, name, event_date, cities(name)")) {
                order("event_date", Order.ASCENDING)
            }
            .decodeList<EventWithCity>()
    }
}