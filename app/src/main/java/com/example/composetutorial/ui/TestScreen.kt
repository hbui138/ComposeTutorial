package com.example.composetutorial.ui

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.composetutorial.R
import com.example.composetutorial.data.SampleData
import com.example.composetutorial.data.UserPreferences
import com.example.composetutorial.data.entity.User
import com.example.composetutorial.data.viewmodel.MainViewModel
import com.example.composetutorial.ui.theme.ComposeTutorialTheme

data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message, modifier: Modifier = Modifier, profileImageUrl: String? = null) {
    Column(modifier = modifier) { // Apply the main modifier here for the whole MessageCard
        Row(modifier = Modifier.padding(all = 8.dp)) { // Apply a separate modifier for the Row
            Image(
                painter = rememberAsyncImagePainter(profileImageUrl ?: R.drawable.citlali),
                contentDescription = "Profile picture",
                modifier = Modifier
                    // Set image size to 40 dp
                    .size(40.dp)
                    // Clip image to be shaped as a circle
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )

            // Add a horizontal space between the image and the column
            Spacer(modifier = Modifier.width(8.dp))

            // We keep track if the message is expanded or not in this
            // variable
            var isExpanded by remember { mutableStateOf(false) }
            // surfaceColor will be updated gradually from one color to the other
            val surfaceColor by animateColorAsState(
                if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            )

            // We toggle the isExpanded variable when we click on this Column
            Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
                Text(
                    text = msg.author,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 1.dp,
                    color = surfaceColor,
                    modifier = Modifier.animateContentSize().padding(1.dp)
                ) {
                    Text(
                        text = msg.body,
                        modifier = Modifier.padding(all = 4.dp),
                        // If the message is expanded, we display all its content
                        // otherwise we only display the first line
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun Conversation(messages: List<Message>, profileImageUrl: String?) {
    LazyColumn (
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(messages) { message ->
            MessageCard(message, profileImageUrl = profileImageUrl)
        }
    }
}

@Composable
fun TestScreen(navController: NavHostController) {
    val mainViewModel: MainViewModel = viewModel()

    // Fetch user data by ID from Room
    var user by remember { mutableStateOf<User?>(null) }  // Fetch user from database


    LaunchedEffect(Unit) {
        // Assuming getUserById() is a suspend function
        user = mainViewModel.getUserById(1)
        Log.d("TestScreen", "Fetched user: $user")
    }

    ComposeTutorialTheme {
        Surface(modifier = Modifier.systemBarsPadding()) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                val updatedMessages = SampleData.conversationSample.map { message ->
                    message.copy(author = user?.username ?: "Default")
                }

                Conversation(
                    messages = updatedMessages,
                    profileImageUrl = user?.profileImageUrl
                )

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
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeTutorialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            MessageCard(
                msg = Message("Citlali", "Welcome to Android Studio"),
                modifier = Modifier.padding(top = 30.dp)
            )
        }
    }
}