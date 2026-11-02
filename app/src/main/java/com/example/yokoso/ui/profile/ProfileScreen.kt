package com.example.yokoso.ui.profile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yokoso.ui.common.LangToggle
import com.example.yokoso.ui.common.Str
import com.example.yokoso.ui.common.tr
import com.example.yokoso.ui.theme.AccentBg
import com.example.yokoso.ui.theme.AccentRed
import com.example.yokoso.ui.theme.BorderColor
import com.example.yokoso.ui.theme.TextSecondary

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = viewModel(),
    onLogout: () -> Unit = {}
) {
    LaunchedEffect(Unit) { viewModel.load() }

    val initials = viewModel.name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .mapNotNull { it.firstOrNull() }
        .joinToString("")
        .uppercase()

    Column(Modifier.fillMaxSize().padding(horizontal = 18.dp)) {
        Spacer(Modifier.height(18.dp))
        Text(tr(Str.NavProfile), style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(18.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(52.dp).background(AccentBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(initials, color = AccentRed, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(viewModel.name, style = MaterialTheme.typography.titleMedium)
                Text(viewModel.email, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }

        Spacer(Modifier.height(24.dp))
        Text(
            tr(Str.Settings).uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = TextSecondary
        )
        Spacer(Modifier.height(8.dp))

        SettingsRow {
            Text(tr(Str.InterfaceLang))
            LangToggle()
        }
        HorizontalDivider(color = BorderColor)

        SettingsRow {
            Text(tr(Str.Notifications))
            Text("✓", color = TextSecondary)
        }
        HorizontalDivider(color = BorderColor)

        SettingsRow(Modifier.clickable { viewModel.logout(onLogout) }) {
            Text(tr(Str.Logout), color = AccentRed)
            Text("›", color = TextSecondary)
        }
    }
}

@Composable
private fun SettingsRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) { content() }
}