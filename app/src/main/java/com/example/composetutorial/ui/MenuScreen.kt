package com.example.composetutorial.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.composetutorial.R
import com.example.composetutorial.Screen
import com.example.composetutorial.ui.theme.ComposeTutorialTheme

@Composable
fun MenuScreen(navController: NavHostController) {
    Surface(modifier = Modifier.systemBarsPadding()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(5.dp))

            Image(
                painter = painterResource(R.drawable.menuimage),
                contentDescription = "Main menu image",
                modifier = Modifier
                    .height(200.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = {
                    navController.navigate(Screen.Test.route) {
                        // Clear the back stack up to the "Menu" screen before navigating to the "Test" screen
                        popUpTo(Screen.Menu.route) { inclusive = false }
                    }
                },
                modifier = Modifier
                    .width(200.dp)
            ) {
                Text("Go to Test Page")
            }

        }
    }

}

@Composable
fun ProfileButton(navController: NavController) {
    Button(
        onClick = {
            navController.navigate(Screen.Profile.route) {
                // Clear the back stack up to the "Menu" screen before navigating to the "Test" screen
                popUpTo(Screen.Menu.route) { inclusive = false }
            }
        },
    ) {
        Image(painter = painterResource(R.drawable.accounticon),
            contentDescription = "Profile picture",
            modifier = Modifier
                // Set image size to 40 dp
                .size(40.dp)
                // Clip image to be shaped as a circle
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
            )
    }
}
@Preview
@Composable
fun PreviewMenuScreen() {
    ComposeTutorialTheme {
        // Remember a NavController for preview
        val navController = rememberNavController()

        // Pass the navController to MenuScreen
        MenuScreen(navController = navController)
    }
}