package com.example.yokoso.ui.events

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yokoso.data.model.EventWithCity
import com.example.yokoso.ui.common.Str
import com.example.yokoso.ui.common.tr
import com.example.yokoso.ui.theme.AccentRed
import com.example.yokoso.ui.theme.BorderColor
import com.example.yokoso.ui.theme.SurfaceSecondary
import com.example.yokoso.ui.theme.TextSecondary

@Composable
fun EventsScreen(
    viewModel: EventsViewModel = viewModel(),
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        Spacer(Modifier.height(18.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
            }
            Text(tr(Str.EventsTitle), style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(14.dp))

        when (val state = viewModel.uiState) {
            is EventsUiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }

            is EventsUiState.Error -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("Ошибка: ${state.message}")
            }

            is EventsUiState.Success -> {
                if (state.events.isEmpty()) {
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text(tr(Str.NoEvents), color = TextSecondary)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(state.events) { event -> EventCard(event) }
                        item { Spacer(Modifier.height(12.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun EventCard(event: EventWithCity) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(SurfaceSecondary, RoundedCornerShape(12.dp))
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Text(formatDate(event.event_date), style = MaterialTheme.typography.labelLarge, color = AccentRed)
        Spacer(Modifier.height(4.dp))
        Text(event.name, style = MaterialTheme.typography.titleSmall)
        event.cities?.name?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}

// превращает 2027-07-17 в 17.07.2027
private fun formatDate(raw: String?): String {
    val value = raw ?: return ""
    val p = value.split("-")
    return if (p.size == 3) "${p[2]}.${p[1]}.${p[0]}" else value
}