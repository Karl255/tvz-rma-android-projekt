package com.tvz.kbistrick.ffmediatools.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ErrorDialog(message: String, onDismiss: () -> Unit, modifier: Modifier = Modifier) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onDismiss) {
                Text("Dismiss")
            }
        },
        title = {
            Text("Error")
        },
        text = {
            Text(message)
        }
    )
}