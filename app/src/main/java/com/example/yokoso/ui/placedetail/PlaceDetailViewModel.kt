package com.example.yokoso.ui.placedetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yokoso.data.model.PlaceWithDetails
import com.example.yokoso.data.model.ReviewWithAuthor
import com.example.yokoso.data.repository.FavoritesRepository
import com.example.yokoso.data.repository.PlacesRepository
import com.example.yokoso.data.repository.ReviewsRepository
import com.example.yokoso.data.repository.RoutesRepository
import kotlinx.coroutines.launch

sealed interface PlaceDetailUiState {
    object Loading : PlaceDetailUiState
    data class Success(val place: PlaceWithDetails, val reviews: List<ReviewWithAuthor>) : PlaceDetailUiState
    data class Error(val message: String) : PlaceDetailUiState
}

class PlaceDetailViewModel(private val placeId: String) : ViewModel() {
    private val placesRepo = PlacesRepository()
    private val reviewsRepo = ReviewsRepository()
    private val favoritesRepo = FavoritesRepository()
    private val routesRepo = RoutesRepository()

    var uiState by mutableStateOf<PlaceDetailUiState>(PlaceDetailUiState.Loading)
        private set
    var isFavorite by mutableStateOf(false)
        private set
    var isInRoute by mutableStateOf(false)
        private set



    fun load() {
        viewModelScope.launch {
            uiState = PlaceDetailUiState.Loading
            uiState = try {
                val place = placesRepo.getPlace(placeId)
                val reviews = reviewsRepo.getReviews(placeId)
                isFavorite = placeId in favoritesRepo.getFavoriteIds()
                isInRoute = routesRepo.getRoutePlaces().any { it.id == placeId }
                PlaceDetailUiState.Success(place, reviews)
            } catch (e: Exception) {
                PlaceDetailUiState.Error(e.message ?: "Не удалось загрузить место")
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            try {
                if (isFavorite) favoritesRepo.remove(placeId) else favoritesRepo.add(placeId)
                isFavorite = !isFavorite
            } catch (_: Exception) { }
        }
    }

    fun addToRoute() {
        viewModelScope.launch {
            try {
                routesRepo.addPlace(placeId)
                isInRoute = true
            } catch (_: Exception) { }
        }
    }
}