package com.example.yokoso.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yokoso.data.repository.AuthRepository
import com.example.yokoso.data.repository.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepo: ProfileRepository = ProfileRepository(),
    private val authRepo: AuthRepository = AuthRepository()
) : ViewModel() {

    var name by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set

    fun load() {
        email = profileRepo.getEmail()
        viewModelScope.launch {
            try {
                name = profileRepo.getName()
            } catch (_: Exception) { }
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                authRepo.signOut()
            } catch (_: Exception) { }
            onDone()
        }
    }
}