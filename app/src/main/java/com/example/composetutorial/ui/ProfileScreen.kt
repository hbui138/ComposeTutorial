package com.example.composetutorial.ui

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.composetutorial.R
import com.example.composetutorial.data.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun ProfileScreen(navController: NavHostController) {
    val mainViewModel: MainViewModel = viewModel()
    val coroutineScope = rememberCoroutineScope()

    var currentUsername by remember { mutableStateOf("") } // Displayed username
    var newUsername by remember { mutableStateOf("") } // Entered username
    var newImageUrl by remember { mutableStateOf("") } // Entered Image URL

    val context = LocalContext.current

    // Launch the photo picker activity for selecting an image
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            coroutineScope.launch(Dispatchers.IO) {
                // Get the input stream for the selected image
                val inputStream = context.contentResolver.openInputStream(it)

                // Specify a location to save the image (in internal storage)
                val outputFile = File(context.filesDir, "profile_picture_${System.currentTimeMillis()}.jpg")

                inputStream?.use { input ->
                    outputFile.outputStream().use { output ->
                        input.copyTo(output) // Copy the selected image to internal storage
                    }
                }

                // Update the new image URL or path for the user profile
                val imagePath = outputFile.absolutePath
                mainViewModel.updateProfileImageUrl(1, imagePath) // Save the new image URL in Room
                newImageUrl = imagePath // Update UI after saving
            }
        }
    }

    // Load User 1's username from the database
    LaunchedEffect(Unit) {
        val user = mainViewModel.getUserById(1)
        currentUsername = user?.username ?: ""
        newImageUrl = user?.profileImageUrl ?: ""
    }

    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        Column() {
            Text(
                text = "Current Username: $currentUsername",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (newImageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(newImageUrl),
                    contentDescription = "Profile picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .clickable { photoPickerLauncher.launch(PickVisualMediaRequest()) } // Launch photo picker when clicked
                )
            } else {
                // Placeholder image if no image is available
                Image(
                    painter = painterResource(id = R.drawable.citlali),
                    contentDescription = "Profile picture placeholder",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .clickable { photoPickerLauncher.launch(PickVisualMediaRequest()) } // Launch photo picker when clicked
                )
            }


            // Input field for the new username
            OutlinedTextField(
                value = newUsername,
                onValueChange = { newUsername = it }, // Update newUsername as the user types
                label = { Text("Enter new username") },
                modifier = Modifier.width(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Button to update the username
            Button(
                onClick = {
                    // Update the UserData username when the button is clicked
                    if (newUsername.isNotBlank()) {
                        coroutineScope.launch {
                            mainViewModel.updateUsername(1, newUsername)
                            currentUsername = newUsername // Only update UI after confirmation
                            newUsername = "" // Clear input field after updating
                        }
                    }
                },
                modifier = Modifier.width(200.dp)
            ) {
                Text("Confirm")
            }
        }

        Button(
            onClick = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter) // Position button at the bottom
                .padding(16.dp)
                .width(200.dp)
        ) {
            Text("Go Back")
        }
    }

}

