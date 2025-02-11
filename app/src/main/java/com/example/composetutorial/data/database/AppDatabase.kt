package com.example.composetutorial.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.composetutorial.data.dao.UserDao
import com.example.composetutorial.data.entity.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}