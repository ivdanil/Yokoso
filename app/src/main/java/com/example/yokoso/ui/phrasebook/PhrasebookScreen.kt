package com.example.yokoso.ui.phrasebook

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.yokoso.data.PHRASEBOOK
import com.example.yokoso.data.Phrase
import com.example.yokoso.ui.common.LanguageState
import com.example.yokoso.ui.common.Str
import com.example.yokoso.ui.common.tr
import com.example.yokoso.ui.theme.AccentRed
import com.example.yokoso.ui.theme.BorderColor
import com.example.yokoso.ui.theme.SurfaceSecondary
import com.example.yokoso.ui.theme.TextSecondary

@Composable
fun PhrasebookScreen(onBack: () -> Unit) {
    // язык подписей берётся из общего состояния, как и в остальном приложении
    val ru = LanguageState.current == "ru"

    LazyColumn(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        item {
            Spacer(Modifier.height(18.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                }
                Text(tr(Str.PhrasebookTitle), style = MaterialTheme.typography.titleLarge)
            }
        }

        PHRASEBOOK.forEach { group ->
            item {
                Spacer(Modifier.height(18.dp))
                Text(
                    (if (ru) group.ru else group.en).uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
                Spacer(Modifier.height(8.dp))
            }
            items(group.phrases) { phrase ->
                PhraseCard(phrase, ru)
                Spacer(Modifier.height(10.dp))
            }
        }

        item { Spacer(Modifier.height(12.dp)) }
    }
}

@Composable
private fun PhraseCard(phrase: Phrase, ru: Boolean) {
    Column(
        Modifier
            .fillMaxWidth()
            .background(SurfaceSecondary, RoundedCornerShape(12.dp))
            .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        // перевод на язык интерфейса
        Text(if (ru) phrase.ru else phrase.en, style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(4.dp))
        // фраза по-японски — её можно показать собеседнику
        Text(phrase.jp, style = MaterialTheme.typography.titleMedium, color = AccentRed)
        // произношение русскими/латинскими буквами
        Text(phrase.romaji, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
    }
}