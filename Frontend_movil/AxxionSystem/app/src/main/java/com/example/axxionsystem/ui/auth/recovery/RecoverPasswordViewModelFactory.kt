package com.example.axxionsystem.ui.auth.recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.axxionsystem.data.repository.auth.AuthRepository

class RecoverPasswordViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecoverPasswordViewModel::class.java)) {
            return RecoverPasswordViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

