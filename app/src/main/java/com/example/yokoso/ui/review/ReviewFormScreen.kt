package com.example.yokoso.ui.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.yokoso.ui.common.Str
import com.example.yokoso.ui.common.tr
import com.example.yokoso.ui.theme.AccentRed
import com.example.yokoso.ui.theme.BorderColor
import com.example.yokoso.ui.theme.SurfaceSecondary
import com.example.yokoso.ui.theme.TextSecondary

@Composable
fun ReviewFormScreen(placeId: String, onBack: () -> Unit) {
    val viewModel: ReviewFormViewModel = viewModel(
        key = "review-$placeId",
        factory = viewModelFactory { initializer { ReviewFormViewModel(placeId) } }
    )

    // после успешной отправки возвращаемся на карточку места
    LaunchedEffect(viewModel.isDone) {
        if (viewModel.isDone) onBack()
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(18.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
            }
            Text(tr(Str.WriteReview), style = MaterialTheme.typography.titleLarge)
        }

        Spacer(Modifier.height(24.dp))

        Text(
            tr(Str.RatePlace),
            color = TextSecondary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(10.dp))

        // пять звёзд: тап по звезде выставляет оценку от 1 до 5
        Row(
            Modifier.align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            for (i in 1..5) {
                val filled = i <= viewModel.rating
                Text(
                    if (filled) "★" else "☆",
                    fontSize = 36.sp,
                    color = if (filled) AccentRed else BorderColor,
                    modifier = Modifier.clickable { viewModel.onRatingChange(i) }
                )
            }
        }

        if (viewModel.ratingMissing) {
            Spacer(Modifier.height(6.dp))
            Text(
                tr(Str.PickRating),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Spacer(Modifier.height(24.dp))

        Text(tr(Str.CommentLabel), style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = viewModel.text,
            onValueChange = viewModel::onTextChange,
            placeholder = { Text(tr(Str.CommentHint)) },
            modifier = Modifier.fillMaxWidth().height(140.dp),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = SurfaceSecondary,
                focusedContainerColor = SurfaceSecondary,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = AccentRed
            )
        )

        viewModel.error?.let {
            Spacer(Modifier.height(10.dp))
            Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = viewModel::submit,
            enabled = !viewModel.isSubmitting,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentRed)
        ) {
            Text(tr(Str.PublishReview), color = Color.White)
        }

        Spacer(Modifier.height(24.dp))
    }
}