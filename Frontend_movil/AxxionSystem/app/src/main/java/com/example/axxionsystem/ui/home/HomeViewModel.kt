package com.example.axxionsystem.ui.home

import com.example.axxionsystem.data.model.auth.UserProfileResponse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.axxionsystem.data.repository.auth.AuthRepository
import com.example.axxionsystem.data.repository.home.DashboardRepository
import com.example.axxionsystem.ui.home.summary.KpiUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val perfil: UserProfileResponse) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel(private val repository: AuthRepository, private val repositoryKpi: DashboardRepository) : ViewModel() {

    private val _uiStateKpi = MutableStateFlow<KpiUiState>(KpiUiState.Loading)
    val uiStateKpi: StateFlow<KpiUiState> = _uiStateKpi.asStateFlow()
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchKpis()
    }

    fun fetchUserProfile() {
        _uiState.value = HomeUiState.Loading

        viewModelScope.launch {
            try {
                val response = repository.getPerfil()

                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = HomeUiState.Success(response.body()!!)
                } else {
                    _uiState.value = HomeUiState.Error("No se pudo cargar el perfil (${response.code()})")
                }
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Error de red: Verifica tu conexion")
            }
        }
    }

    fun logoutBackend() {
        viewModelScope.launch {
            try {
                repository.logout()
            } catch (e: Exception) {
            }
        }
    }

    private fun fetchKpis() {
        viewModelScope.launch {
            _uiStateKpi.value = KpiUiState.Loading

            try {
                val result = repositoryKpi.getDashboardKpis()
                _uiStateKpi.value = KpiUiState.Success(result)
            } catch (e: Exception) {
                _uiStateKpi.value = KpiUiState.Error("Error al cargar datos")
            }
        }
    }

}
