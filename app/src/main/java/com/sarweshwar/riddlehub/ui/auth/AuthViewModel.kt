package com.sarweshwar.riddlehub.ui.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarweshwar.riddlehub.data.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    val loggedIn = mutableStateOf(authRepository.getCurrentUser() != null)
    val loading = mutableStateOf(false)

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        loading.value = true
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            result.onSuccess {
                loggedIn.value = true
                onResult(true, null)
            }.onFailure {
                onResult(false, it.message)
            }
            loading.value = false
        }
    }

    fun register(
        email: String,
        username: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        loading.value = true
        viewModelScope.launch {
            val result = authRepository.register(email, username, password)
            result.onSuccess {
                loggedIn.value = true
                onResult(true, null)
            }.onFailure {
                onResult(false, it.message)
            }
            loading.value = false
        }
    }

    fun logout() {
        authRepository.logout()
        loggedIn.value = false
    }
}
