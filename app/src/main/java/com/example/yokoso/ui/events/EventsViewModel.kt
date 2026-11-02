package com.example.yokoso.ui.events

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yokoso.data.model.EventWithCity
import com.example.yokoso.data.repository.EventsRepository
import kotlinx.coroutines.launch

sealed interface EventsUiState {
    object Loading : EventsUiState
    data class Success(val events: List<EventWithCity>) : EventsUiState
    data class Error(val message: String) : EventsUiState
}

class EventsViewModel(
    private val repository: EventsRepository = EventsRepository()
) : ViewModel() {

    var uiState by mutableStateOf<EventsUiState>(EventsUiState.Loading)
        private set

    init { load() }

    fun load() {
        viewModelScope.launch {
            uiState = EventsUiState.Loading
            uiState = try {
                EventsUiState.Success(repository.getEvents())
            } catch (e: Exception) {
                EventsUiState.Error(e.message ?: "Не удалось загрузить события")
            }
        }
    }
}