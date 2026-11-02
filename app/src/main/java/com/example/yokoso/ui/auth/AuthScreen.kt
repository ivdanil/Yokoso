package com.example.yokoso.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.yokoso.ui.common.Str
import com.example.yokoso.ui.common.tr
import com.example.yokoso.ui.theme.AccentRed
import com.example.yokoso.ui.theme.SurfaceSecondary
import com.example.yokoso.ui.theme.TextSecondary

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onAuthSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    LaunchedEffect(viewModel.uiState) {
        if (viewModel.uiState is AuthUiState.Success) onAuthSuccess()
    }

    Column(
        Modifier.fillMaxSize().padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Yokoso", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(4.dp))
        Text(tr(Str.Tagline), style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        Spacer(Modifier.height(26.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .background(SurfaceSecondary, RoundedCornerShape(50))
                .padding(4.dp)
        ) {
            TabPill(tr(Str.TabLogin), viewModel.isLoginMode, Modifier.weight(1f)) {
                if (!viewModel.isLoginMode) viewModel.toggleMode()
            }
            TabPill(tr(Str.TabRegister), !viewModel.isLoginMode, Modifier.weight(1f)) {
                if (viewModel.isLoginMode) viewModel.toggleMode()
            }
        }
        Spacer(Modifier.height(24.dp))

        if (!viewModel.isLoginMode) {
            StyledField(name, { name = it }, tr(Str.FieldName))
            Spacer(Modifier.height(12.dp))
        }
        StyledField(email, { email = it }, tr(Str.FieldEmail), KeyboardType.Email)
        Spacer(Modifier.height(12.dp))
        StyledField(password, { password = it }, tr(Str.FieldPassword), KeyboardType.Password, isPassword = true)

        if (viewModel.isLoginMode) {
            Spacer(Modifier.height(10.dp))
            Text(
                tr(Str.Forgot),
                color = AccentRed,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.End)
            )
        }
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { viewModel.submit(email, password, name) },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
            enabled = viewModel.uiState !is AuthUiState.Loading
        ) {
            Text(if (viewModel.isLoginMode) tr(Str.BtnLogin) else tr(Str.BtnRegister), color = Color.White)
        }

        when (val state = viewModel.uiState) {
            is AuthUiState.Loading -> CircularProgressIndicator(Modifier.padding(top = 16.dp))
            is AuthUiState.Error -> Text(
                state.message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 12.dp)
            )
            else -> {}
        }
    }
}

@Composable
private fun TabPill(text: String, active: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier
            .background(if (active) AccentRed else Color.Transparent, RoundedCornerShape(50))
            .clickable { onClick() }
            .padding(vertical = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = if (active) Color.White else TextSecondary, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
private fun StyledField(
    value: String, onValueChange: (String) -> Unit, label: String,
    keyboardType: KeyboardType = KeyboardType.Text, isPassword: Boolean = false
) {
    Column(Modifier.fillMaxWidth()) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = SurfaceSecondary,
                focusedContainerColor = SurfaceSecondary,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = AccentRed
            )
        )
    }
}