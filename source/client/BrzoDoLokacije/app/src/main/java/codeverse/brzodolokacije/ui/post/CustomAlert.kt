package codeverse.brzodolokacije.ui.post

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import codeverse.brzodolokacije.ui.theme.primaryColor

@Composable
fun CustomAlert(onDismiss: () -> Unit,
                onConfirm: () -> Unit,
                text: String){
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
            })
            { Text(text = "Da", color = primaryColor) }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            })
            { Text(text = "Ne", color = primaryColor) }
        },
        title = { Text(text = "Potvrdite akciju") },
        text = { Text(text = text ) }
    )
}