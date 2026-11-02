package com.example.yokoso.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yokoso.data.repository.AuthRepository
import kotlinx.coroutines.launch

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    object Success : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel(private val repository: AuthRepository = AuthRepository()) : ViewModel() {
    var uiState by mutableStateOf<AuthUiState>(AuthUiState.Idle)
        private set

    var isLoginMode by mutableStateOf(true)
        private set

    fun toggleMode() {
        isLoginMode = !isLoginMode
    }

    fun submit(email: String, password: String, name: String) {
        viewModelScope.launch {
            uiState = AuthUiState.Loading
            uiState = try {
                if (isLoginMode) repository.signIn(email, password)
                else repository.signUp(email, password, name)
                AuthUiState.Success
            } catch (e: Exception) {
                AuthUiState.Error(e.message ?: "Ошибка авторизации")
            }
        }
    }
}