package com.example.yokoso.ui.favorites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yokoso.data.model.PlaceWithDetails
import com.example.yokoso.data.repository.FavoritesRepository
import kotlinx.coroutines.launch

sealed interface FavoritesUiState {
    object Loading : FavoritesUiState
    data class Success(val places: List<PlaceWithDetails>) : FavoritesUiState
    data class Error(val message: String) : FavoritesUiState
}

class FavoritesViewModel(
    private val repository: FavoritesRepository = FavoritesRepository()
) : ViewModel() {

    var uiState by mutableStateOf<FavoritesUiState>(FavoritesUiState.Loading)
        private set

    fun load() {
        viewModelScope.launch {
            uiState = FavoritesUiState.Loading
            uiState = try {
                FavoritesUiState.Success(repository.getFavoritePlaces())
            } catch (e: Exception) {
                FavoritesUiState.Error(e.message ?: "Не удалось загрузить избранное")
            }
        }
    }
}