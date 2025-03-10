package com.example.econbeacon.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.econbeacon.R
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.example.econbeacon.viewmodel.GameStateViewModel

val MedievalFont = FontFamily(
    Font(R.font.old_london)
)

@Composable
fun MainScreen(
    onMarketClick: () -> Unit,
    gameStateViewModel: GameStateViewModel,

    ) {
    // resource id handler
    var currentAvatar by remember { mutableIntStateOf(R.drawable.avatar_2) }
    // show  up window state
    var showAvatarPicker by remember { mutableStateOf(false) }
    // using box to place elements on top of each other
    Box(modifier = Modifier.fillMaxSize()) {
        // Background, which is kind of the city at the moment
        Image(
            painter = painterResource(id = R.drawable.house2),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Avatar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, end = 16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Image(
                painter = painterResource(currentAvatar),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(4.dp)
                    .clickable { showAvatarPicker = true },
                contentScale = ContentScale.Crop
            )
        }

        // The avatar picker
        if (showAvatarPicker) {
            //  lambda function is passed to AvatarPickerDialog and is called when an avatar is selected.
            // It updates the selectedAvatar variable with the chosen avatar and hides the dialog.
            AvatarPickerDialog(
                onAvatarSelected = { chosenAvatarId ->
                    currentAvatar = chosenAvatarId
                    showAvatarPicker = false
                },
                onDismiss = { showAvatarPicker = false }
            )
        }

        // Button "Market"
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Technically since there is only one button it does not matter, but
            // if there would be more they'd look nicer in a row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onMarketClick() }
                ) {
                    Text(
                        text = "Market",
                        fontFamily = MedievalFont,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}
