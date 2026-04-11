package com.example.axxionsystem.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.axxionsystem.data.repository.auth.AuthRepository
import com.example.axxionsystem.data.repository.home.DashboardRepository

class HomeViewModelFactory(private val repository: AuthRepository, private val repositoryKpi: DashboardRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository, repositoryKpi) as T
        }
        throw IllegalArgumentException("ViewModel desconocido")
    }
}