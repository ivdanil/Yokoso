package com.example.yokoso.ui.home

import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.filled.Translate
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.yokoso.data.model.PlaceWithDetails
import com.example.yokoso.ui.common.LangToggle
import com.example.yokoso.ui.common.Str
import com.example.yokoso.ui.common.tr
import com.example.yokoso.ui.theme.AccentRed
import com.example.yokoso.ui.theme.BorderColor
import com.example.yokoso.ui.theme.SurfaceSecondary
import com.example.yokoso.ui.theme.TextSecondary
import com.example.yokoso.ui.theme.ThumbPlaceholderBrush

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onPlaceClick: (String) -> Unit = {},
    onEventsClick: () -> Unit = {},
    onPhrasebookClick: () -> Unit = {}
) {
    // обновляем избранное при каждом открытии главной (в том числе при возврате с карточки)
    LaunchedEffect(Unit) { viewModel.refreshFavorites() }

    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        Spacer(Modifier.height(18.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onPhrasebookClick) {
                Icon(Icons.Default.Translate, contentDescription = "Разговорник", tint = TextSecondary)
            }
            IconButton(onClick = onEventsClick) {
                Icon(Icons.Default.DateRange, contentDescription = "События", tint = TextSecondary)
            }
            LangToggle()
        }

        Spacer(Modifier.height(14.dp))
        SearchBar(query = viewModel.query, onQueryChange = viewModel::onQueryChange)
        Spacer(Modifier.height(14.dp))
        CategoryChips(selected = viewModel.selectedCategory, onClick = viewModel::onCategoryClick)
        Spacer(Modifier.height(14.dp))

        when (val state = viewModel.uiState) {
            is HomeUiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }

            is HomeUiState.Error -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("Ошибка: ${state.message}")
            }

            is HomeUiState.Success -> {
                val shown = viewModel.filter(state.places)
                if (shown.isEmpty()) {
                    EmptyState(onReset = viewModel::resetFilters)
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(shown) { place ->
                            PlaceCard(
                                place = place,
                                onClick = { onPlaceClick(place.id) },
                                isFavorite = place.id in viewModel.favoriteIds,
                                onFavoriteClick = { viewModel.toggleFavorite(place.id) }
                            )
                        }
                        item { Spacer(Modifier.height(12.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(SurfaceSecondary, RoundedCornerShape(14.dp))
            .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary)
        Spacer(Modifier.width(8.dp))

        Box(Modifier.weight(1f)) {
            // подсказка показывается, пока поле пустое
            if (query.isEmpty()) {
                Text(tr(Str.Search), color = TextSecondary)
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // крестик для очистки поиска
        if (query.isNotEmpty()) {
            Icon(
                Icons.Default.Close,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onQueryChange("") }
            )
        }
    }
}

@Composable
fun CategoryChips(selected: String?, onClick: (String) -> Unit) {
    // пара: ключ перевода для подписи и название категории в БД
    val chips = listOf(
        Str.ChipTemples to "Храм",
        Str.ChipFood to "Ресторан",
        Str.ChipMuseums to "Музей",
        Str.ChipParks to "Парк"
    )
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(chips) { (key, category) ->
            val active = selected == category
            Text(
                tr(key),
                color = if (active) Color.White else TextSecondary,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(if (active) AccentRed else SurfaceSecondary)
                    .border(1.dp, if (active) AccentRed else BorderColor, RoundedCornerShape(50))
                    .clickable { onClick(category) }
                    .padding(horizontal = 13.dp, vertical = 7.dp)
            )
        }
    }
}

@Composable
fun EmptyState(onReset: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, bottom = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier.size(72.dp).background(SurfaceSecondary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("🗺️", style = MaterialTheme.typography.headlineMedium)
        }
        Spacer(Modifier.height(18.dp))
        Text(tr(Str.NothingFound), style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(6.dp))
        Text(
            tr(Str.NothingFoundHint),
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(20.dp))
        OutlinedButton(onClick = onReset) { Text(tr(Str.ResetFilters)) }
    }
}

@Composable
fun PlaceCard(
    place: PlaceWithDetails,
    onClick: () -> Unit,
    isFavorite: Boolean = false,
    onFavoriteClick: () -> Unit = {}
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .background(SurfaceSecondary, RoundedCornerShape(12.dp))
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (place.photo_url != null) {
            AsyncImage(
                model = place.photo_url,
                contentDescription = place.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ThumbPlaceholderBrush)
            )
        } else {
            Box(
                Modifier
                    .size(56.dp)
                    .background(ThumbPlaceholderBrush, RoundedCornerShape(8.dp))
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(place.name, style = MaterialTheme.typography.titleSmall)
            val sub = listOfNotNull(place.categories?.name, place.cities?.name).joinToString(" · ")
            if (sub.isNotEmpty()) {
                Text(sub, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
            Text(tr(Str.Open), style = MaterialTheme.typography.bodySmall, color = AccentRed)
        }

        IconButton(onClick = onFavoriteClick) {
            Icon(
                if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "В избранное",
                tint = if (isFavorite) AccentRed else TextSecondary
            )
        }
    }
}