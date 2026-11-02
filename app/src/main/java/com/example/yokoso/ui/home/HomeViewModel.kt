package com.example.yokoso.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yokoso.data.model.PlaceWithDetails
import com.example.yokoso.data.repository.FavoritesRepository
import com.example.yokoso.data.repository.PlacesRepository
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    object Loading : HomeUiState
    data class Success(val places: List<PlaceWithDetails>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

class HomeViewModel(
    private val repository: PlacesRepository = PlacesRepository()
) : ViewModel() {

    var uiState by mutableStateOf<HomeUiState>(HomeUiState.Loading)
        private set

    // текст в строке поиска
    var query by mutableStateOf("")
        private set

    // выбранная категория (название в БД), null = показывать все
    var selectedCategory by mutableStateOf<String?>(null)
        private set
    private val favoritesRepo = FavoritesRepository()

    // id мест, которые пользователь уже добавил в избранное
    var favoriteIds by mutableStateOf<Set<String>>(emptySet())
        private set

    fun refreshFavorites() {
        viewModelScope.launch {
            try {
                favoriteIds = favoritesRepo.getFavoriteIds()
            } catch (_: Exception) { }
        }
    }

    // добавляет место в избранное или убирает его
    fun toggleFavorite(placeId: String) {
        viewModelScope.launch {
            try {
                if (placeId in favoriteIds) {
                    favoritesRepo.remove(placeId)
                    favoriteIds = favoriteIds - placeId
                } else {
                    favoritesRepo.add(placeId)
                    favoriteIds = favoriteIds + placeId
                }
            } catch (_: Exception) { }
        }
    }

    init { loadPlaces() }

    fun loadPlaces() {
        viewModelScope.launch {
            uiState = HomeUiState.Loading
            uiState = try {
                HomeUiState.Success(repository.getPlaces())
            } catch (e: Exception) {
                HomeUiState.Error(e.message ?: "Не удалось загрузить места")
            }
        }
    }

    fun onQueryChange(value: String) {
        query = value
    }

    // повторный тап по выбранной категории снимает фильтр
    fun onCategoryClick(category: String) {
        selectedCategory = if (selectedCategory == category) null else category
    }

    fun resetFilters() {
        query = ""
        selectedCategory = null
    }

    // применяет поиск (по названию и городу) и категорию к списку
    fun filter(places: List<PlaceWithDetails>): List<PlaceWithDetails> {
        val q = query.trim()
        return places.filter { place ->
            val matchesQuery = q.isEmpty() ||
                    place.name.contains(q, ignoreCase = true) ||
                    place.cities?.name?.contains(q, ignoreCase = true) == true
            val matchesCategory =
                selectedCategory == null || place.categories?.name == selectedCategory
            matchesQuery && matchesCategory
        }
    }
}