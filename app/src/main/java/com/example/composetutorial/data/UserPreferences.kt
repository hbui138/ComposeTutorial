package com.example.composetutorial.data

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun getUsername(): String {
        return sharedPreferences.getString("username", "Lexi") ?: "Hieu"
    }

    fun setUsername(username: String) {
        sharedPreferences.edit().putString("username", username).apply()
    }
}