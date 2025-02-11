package com.example.composetutorial.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.composetutorial.data.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE username LIKE :username LIMIT 1")
    fun findByUsername(username: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)

    @Query("UPDATE user SET username = :newUsername WHERE uid = :userId")
    suspend fun updateUsername(userId: Int, newUsername: String)

    @Query("SELECT * FROM user WHERE uid = :userId LIMIT 1")
    fun getUserById(userId: Int): User?

    @Insert
    suspend fun insert(user: User)

    @Query("UPDATE user SET profile_image_url = :imagePath WHERE uid = :userId")
    suspend fun updateProfileImageUrl(userId: Int, imagePath: String)
}