package com.example.yokoso.ui.route

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yokoso.ui.common.Str
import com.example.yokoso.ui.common.tr
import com.example.yokoso.ui.theme.AccentRed
import com.example.yokoso.ui.theme.BorderColor
import com.example.yokoso.ui.theme.TextSecondary

@Composable
fun RouteScreen(
    viewModel: RouteViewModel = viewModel(),
    onPlaceClick: (String) -> Unit = {}
) {
    LaunchedEffect(Unit) { viewModel.load() }

    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        Spacer(Modifier.height(18.dp))
        Text(tr(Str.NavRoute), style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(14.dp))

        when (val state = viewModel.uiState) {
            is RouteUiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }

            is RouteUiState.Error -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("Ошибка: ${state.message}")
            }

            is RouteUiState.Success -> {
                if (state.places.isEmpty()) {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text(tr(Str.NoRoute), color = TextSecondary)
                    }
                } else {
                    LazyColumn {
                        itemsIndexed(state.places) { index, place ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable { onPlaceClick(place.id) }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    Modifier.size(24.dp).background(AccentRed, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "${index + 1}",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(place.name, style = MaterialTheme.typography.titleSmall)
                                    val sub = listOfNotNull(place.categories?.name, place.cities?.name)
                                        .joinToString(" · ")
                                    if (sub.isNotEmpty()) {
                                        Text(sub, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                    }
                                }
                                IconButton(onClick = { viewModel.remove(place.id) }) {
                                    Icon(Icons.Default.Close, contentDescription = "Удалить", tint = TextSecondary)
                                }
                            }
                            HorizontalDivider(color = BorderColor)
                        }
                    }
                }
            }
        }
    }
}