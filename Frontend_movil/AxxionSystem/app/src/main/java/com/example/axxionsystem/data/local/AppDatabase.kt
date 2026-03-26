package com.example.axxionsystem.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.axxionsystem.data.local.dao.ProductoDao
import com.example.axxionsystem.data.local.dao.UserDao
import com.example.axxionsystem.data.local.entity.UserEntity
import com.example.axxionsystem.data.model.producto.ProductoEntity

@Database([UserEntity::class, ProductoEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun productoDao(): ProductoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sistemarenta"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}