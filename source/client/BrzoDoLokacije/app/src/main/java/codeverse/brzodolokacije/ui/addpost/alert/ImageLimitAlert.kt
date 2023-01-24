package codeverse.brzodolokacije.ui.addpost.alert

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import codeverse.brzodolokacije.ui.theme.dangerColor
import codeverse.brzodolokacije.ui.theme.primaryColor

@Composable
fun ImageLimitAlert(onDismiss: () -> Unit){
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
            })
            { Text(text = "OK", color = primaryColor) }
        },
        title = { Text(text = "Obaveštenje") },
        text = { Text(text = "Možete dodati najviše 5 slika", color = dangerColor) }
    )
}
