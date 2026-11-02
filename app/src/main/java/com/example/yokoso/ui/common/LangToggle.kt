package com.example.yokoso.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.yokoso.ui.theme.AccentRed
import com.example.yokoso.ui.theme.BorderColor
import com.example.yokoso.ui.theme.TextSecondary

@Composable
fun LangToggle() {
    Row(Modifier.border(1.dp, BorderColor, RoundedCornerShape(50)).padding(2.dp)) {
        Text(
            "RU",
            color = if (LanguageState.current == "ru") Color.White else TextSecondary,
            modifier = Modifier
                .background(
                    if (LanguageState.current == "ru") AccentRed else Color.Transparent,
                    RoundedCornerShape(50)
                )
                .clickable { LanguageState.set("ru") }
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )
        Text(
            "EN",
            color = if (LanguageState.current == "en") Color.White else TextSecondary,
            modifier = Modifier
                .background(
                    if (LanguageState.current == "en") AccentRed else Color.Transparent,
                    RoundedCornerShape(50)
                )
                .clickable { LanguageState.set("en") }
                .padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}