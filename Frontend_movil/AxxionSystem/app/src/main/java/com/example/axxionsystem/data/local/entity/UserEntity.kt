package com.example.axxionsystem.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("user_profile")
data class UserEntity(
    @PrimaryKey
    val localId: Int = 1,
    val serverId: Int,
    val nombre: String,
    val email: String,
    val rol: String
)
