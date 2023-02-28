package com.example.hilt.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hilt.database.dao.UserDao
import com.example.hilt.database.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

abstract fun userDao(): UserDao
}