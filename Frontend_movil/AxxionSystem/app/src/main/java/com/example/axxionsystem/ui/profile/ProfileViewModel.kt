package com.example.axxionsystem.ui.profile

import com.example.axxionsystem.data.local.entity.UserEntity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.axxionsystem.data.repository.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val user: UserEntity) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}

class ProfileViewModel(private val repository: UserRepository): ViewModel() {

    val uiState: StateFlow<ProfileUiState> = repository.getUserProfileFlow()
        .map { userEntity ->
            if (userEntity != null) {
                ProfileUiState.Success(userEntity)
            } else {
                ProfileUiState.Loading
            }
        }
        .catch { e ->
            emit(ProfileUiState.Error("Error al cargar datos locales: ${e.message}"))
        }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileUiState.Loading
        )

    fun syncProfile() {
        viewModelScope.launch {
            repository.syncProfileBackground()
        }
    }
}