package com.example.econbeacon.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.econbeacon.R

@Composable
fun AvatarPickerDialog(
    onAvatarSelected: (Int) -> Unit,
    onDismiss: () -> Unit
) {

    val avatarList = listOf(
        R.drawable.avatar_1,
        R.drawable.avatar_2,
        R.drawable.avatar_4,
        R.drawable.avatar_5,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose Avatar") },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                avatarList.forEach { avatar ->
                    Image(
                        painter = painterResource(id = avatar),
                        contentDescription = "Avatar Option",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .clickable { onAvatarSelected(avatar) },
                        contentScale = ContentScale.Crop
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
