package com.example.yokoso.ui.route

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yokoso.data.model.PlaceWithDetails
import com.example.yokoso.data.repository.RoutesRepository
import kotlinx.coroutines.launch

sealed interface RouteUiState {
    object Loading : RouteUiState
    data class Success(val places: List<PlaceWithDetails>) : RouteUiState
    data class Error(val message: String) : RouteUiState
}

class RouteViewModel(
    private val repository: RoutesRepository = RoutesRepository()
) : ViewModel() {

    var uiState by mutableStateOf<RouteUiState>(RouteUiState.Loading)
        private set

    fun load() {
        viewModelScope.launch {
            uiState = RouteUiState.Loading
            uiState = try {
                RouteUiState.Success(repository.getRoutePlaces())
            } catch (e: Exception) {
                RouteUiState.Error(e.message ?: "Не удалось загрузить маршрут")
            }
        }
    }

    fun remove(placeId: String) {
        viewModelScope.launch {
            try {
                repository.removePlace(placeId)
                uiState = RouteUiState.Success(repository.getRoutePlaces())
            } catch (e: Exception) {
                uiState = RouteUiState.Error(e.message ?: "Не удалось удалить")
            }
        }
    }
}