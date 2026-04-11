package com.example.axxionsystem.ui.home.summary

import com.example.axxionsystem.data.model.resumen.ResumenResponse

sealed class KpiUiState {
    object Loading: KpiUiState()
    data class Success(val data: ResumenResponse): KpiUiState()
    data class Error(val message: String): KpiUiState()
}