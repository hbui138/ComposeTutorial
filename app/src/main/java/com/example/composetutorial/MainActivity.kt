package com.example.composetutorial

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope

import androidx.navigation.compose.rememberNavController
import com.example.composetutorial.data.UserPreferences
import com.example.composetutorial.data.viewmodel.MainViewModel
import com.example.composetutorial.ui.ProfileButton
import com.example.composetutorial.ui.theme.ComposeTutorialTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var sensorManager: SensorManager
    private var temperatureSensor: Sensor? = null
    private val temperatureViewModel: TemperatureViewModel by viewModels()
    private var temperatureThreshold = 30f
    private var notificationSent = false

    private val temperatureListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val temperature = event.values[0]
            temperatureViewModel.updateTemperature(temperature)
            checkTemperatureThreshold(temperature)
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
        if (temperatureSensor == null) {
            simulateTemperature()
        }

        enableEdgeToEdge()

        mainViewModel.insertTestUser()

        setContent {
            ComposeTutorialTheme {
                Surface(modifier = Modifier.systemBarsPadding()) {
                    MyApp(mainViewModel = mainViewModel, temperatureViewModel = temperatureViewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        temperatureSensor?.let {
            sensorManager.registerListener(temperatureListener, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(temperatureListener)
    }

    private fun checkTemperatureThreshold(currentTemp: Float) {
        if (currentTemp >= temperatureThreshold && !notificationSent) {
            showTemperatureAlertNotification()
            notificationSent = true
        } else if (currentTemp < temperatureThreshold) {
            notificationSent = false
        }
    }

    private fun simulateTemperature() {
        lifecycleScope.launch {
            var currentTemp = 20f
            while (true) {
                delay(5000)
                currentTemp += (Math.random() * 4 - 2).toFloat()
                temperatureViewModel.updateTemperature(currentTemp)
                checkTemperatureThreshold(currentTemp)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "temperature_channel",
                "Temperature Alerts",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alerts when temperature reaches a certain level"
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showTemperatureAlertNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, "temperature_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Temperature Alert")
            .setContentText("Temperature has reached the threshold!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(mainViewModel: MainViewModel, temperatureViewModel: TemperatureViewModel) {
    val navController = rememberNavController()
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Compose Tutorial",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    ProfileButton(navController = navController)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier.padding(innerPadding),
            ) {
            AppNavGraph(
                navController = navController,
                temperatureViewModel = temperatureViewModel
            )
        }

    }
}

