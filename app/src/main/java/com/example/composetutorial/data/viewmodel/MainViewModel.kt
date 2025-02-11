package com.example.composetutorial.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.composetutorial.data.database.AppDatabase
import com.example.composetutorial.data.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val db: AppDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "database-name"
    ).build()

    private val userDao = db.userDao()

    // Fetch all users (already using IO correctly)
    suspend fun getUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            userDao.getAll()
        }
    }

    // Fetch user by username (already correct)
    suspend fun getUserByUsername(username: String): User? {
        return withContext(Dispatchers.IO) {
            userDao.findByUsername(username)
        }
    }

    // Update the username using IO
    suspend fun updateUsername(userId: Int, newUsername: String) {
        withContext(Dispatchers.IO) {
            userDao.updateUsername(userId, newUsername)
        }
    }

    // Insert a test user (runs in background)
    fun insertTestUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val existingUser = userDao.getUserById(1)
            if (existingUser == null) {
                val testUser = User(uid = 1, username = "TestUser", profileImageUrl = null)
                userDao.insert(testUser)
            }
        }
    }

    // Fetch user by ID in background
    suspend fun getUserById(userId: Int): User? {
        return withContext(Dispatchers.IO) {
            userDao.getUserById(userId)
        }
    }

    // Update profile image URL (explicitly using Dispatchers.IO)
    fun updateProfileImageUrl(userId: Int, imagePath: String) {
        viewModelScope.launch(Dispatchers.IO) {  // Now runs in background properly
            userDao.updateProfileImageUrl(userId, imagePath)
        }
    }
}
