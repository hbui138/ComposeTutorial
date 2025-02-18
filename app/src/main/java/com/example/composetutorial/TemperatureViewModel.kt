package com.example.composetutorial

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TemperatureViewModel : ViewModel() {
    private val _currentTemperature = MutableStateFlow(0f)
    val currentTemperature: StateFlow<Float> = _currentTemperature.asStateFlow()

    fun updateTemperature(temp: Float) {
        Log.d("TemperatureViewModel", "Updating temperature to $temp")
        _currentTemperature.value = temp
    }
}