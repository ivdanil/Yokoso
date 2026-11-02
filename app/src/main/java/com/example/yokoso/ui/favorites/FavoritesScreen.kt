package com.example.yokoso.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yokoso.ui.common.Str
import com.example.yokoso.ui.common.tr
import com.example.yokoso.ui.home.PlaceCard
import com.example.yokoso.ui.theme.TextSecondary

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = viewModel(),
    onPlaceClick: (String) -> Unit = {}
) {
    // перезагружаем список каждый раз, когда открываем вкладку
    LaunchedEffect(Unit) { viewModel.load() }

    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        Spacer(Modifier.height(18.dp))
        Text(tr(Str.NavFav), style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(14.dp))

        when (val state = viewModel.uiState) {
            is FavoritesUiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }

            is FavoritesUiState.Error -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("Ошибка: ${state.message}")
            }

            is FavoritesUiState.Success -> {
                if (state.places.isEmpty()) {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text(tr(Str.NoFavorites), color = TextSecondary)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(state.places) { place ->
                            PlaceCard(place = place, onClick = { onPlaceClick(place.id) })
                        }
                    }
                }
            }
        }
    }
}