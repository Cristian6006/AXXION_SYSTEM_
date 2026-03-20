package com.example.axxionsystem.data.repository.user

import android.util.Log
import com.example.axxionsystem.data.api.ApiService
import com.example.axxionsystem.data.local.dao.UserDao
import com.example.axxionsystem.data.local.entity.UserEntity
import com.example.axxionsystem.data.model.auth.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(
    private val apiService: ApiService,
    private val userDao: UserDao
) {
    fun getUserProfileFlow(): Flow<UserEntity?> {
        return userDao.getUserProfile()
    }

    suspend fun syncProfileBackground() {
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getPerfil()

                if (response.isSuccessful && response.body() != null) {
                    val remoteProfile = response.body()!!

                    val userEntity = remoteProfile.toEntity()

                    userDao.saveUserProfile(userEntity)
                    Log.d("UserRepository", "Sincronización silenciosa exitosa. Perfil actualizado en BD local.")
                } else {
                    Log.e("UserRepository", "Error en API al sincronizar perfil: ${response.code()}")
                }
            } catch(e: Exception) {
                Log.w("UserRepository", "Fallo de red en sincronización silenciosa (Offline-First en acción): ${e.message}")
            }
        }
    }

    suspend fun clearProfileData() {
        withContext(Dispatchers.IO) {
            userDao.clearProfile()
        }
    }
}