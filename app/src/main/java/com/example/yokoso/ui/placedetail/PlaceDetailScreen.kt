package com.example.yokoso.ui.placedetail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import coil.compose.AsyncImage
import com.example.yokoso.data.model.ReviewWithAuthor
import com.example.yokoso.ui.common.Str
import com.example.yokoso.ui.common.tr
import com.example.yokoso.ui.theme.AccentRed
import com.example.yokoso.ui.theme.BorderColor
import com.example.yokoso.ui.theme.TextSecondary
import com.example.yokoso.ui.theme.ThumbPlaceholderBrush

@Composable
fun PlaceDetailScreen(
    placeId: String,
    onBack: () -> Unit,
    onWriteReview: () -> Unit
) {
    val viewModel: PlaceDetailViewModel = viewModel(
        key = placeId,
        factory = viewModelFactory { initializer { PlaceDetailViewModel(placeId) } }
    )

    // загружаем при каждом открытии экрана (в том числе при возврате с формы отзыва)
    LaunchedEffect(Unit) { viewModel.load() }

    when (val state = viewModel.uiState) {
        is PlaceDetailUiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            CircularProgressIndicator()
        }

        is PlaceDetailUiState.Error -> Box(Modifier.fillMaxSize(), Alignment.Center) {
            Text("Ошибка: ${state.message}")
        }

        is PlaceDetailUiState.Success -> {
            val place = state.place
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                Box(Modifier.fillMaxWidth().height(190.dp).background(ThumbPlaceholderBrush)) {
                    if (place.photo_url != null) {
                        AsyncImage(
                            model = place.photo_url,
                            contentDescription = place.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    RoundIconButton(Modifier.align(Alignment.TopStart).padding(16.dp), onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                    RoundIconButton(
                        Modifier.align(Alignment.TopEnd).padding(16.dp),
                        { viewModel.toggleFavorite() }
                    ) {
                        Icon(
                            if (viewModel.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Избранное",
                            tint = AccentRed
                        )
                    }
                }

                Column(Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
                    Text(place.name, style = MaterialTheme.typography.titleLarge)
                    val sub = listOfNotNull(place.categories?.name, place.cities?.name).joinToString(" · ")
                    if (sub.isNotEmpty()) {
                        Text(sub, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    }

                    place.description?.let {
                        Spacer(Modifier.height(20.dp))
                        SectionTitle("Описание")
                        Text(it, style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(Modifier.height(18.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { viewModel.addToRoute() },
                            enabled = !viewModel.isInRoute,
                            modifier = Modifier.weight(1f).height(46.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
                        ) {
                            Text(
                                if (viewModel.isInRoute) tr(Str.InRoute) else tr(Str.AddToRoute),
                                color = Color.White
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))
                    SectionTitle("Отзывы (${state.reviews.size})")
                    if (state.reviews.isEmpty()) {
                        Text(
                            "Пока нет отзывов",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        state.reviews.forEach { ReviewItem(it) }
                    }

                    Spacer(Modifier.height(14.dp))
                    OutlinedButton(
                        onClick = onWriteReview,
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text(tr(Str.WriteReview))
                    }
                }
            }
        }
    }
}

@Composable
private fun RoundIconButton(modifier: Modifier, onClick: () -> Unit, content: @Composable () -> Unit) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(36.dp).background(Color.White.copy(alpha = 0.85f), CircleShape)
    ) { content() }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text.uppercase(), style = MaterialTheme.typography.labelMedium, color = TextSecondary)
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun ReviewItem(review: ReviewWithAuthor) {
    Column(Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(review.profiles?.name ?: "Пользователь", style = MaterialTheme.typography.titleSmall)
            val stars = review.rating ?: 0
            Text("★".repeat(stars) + "☆".repeat(5 - stars), color = AccentRed)
        }
        review.review_text?.let {
            Text(it, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        HorizontalDivider(Modifier.padding(top = 10.dp), color = BorderColor)
    }
}