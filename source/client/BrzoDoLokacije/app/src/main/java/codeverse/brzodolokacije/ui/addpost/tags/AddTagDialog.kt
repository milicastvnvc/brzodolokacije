package codeverse.brzodolokacije.ui.addpost.tags

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import codeverse.brzodolokacije.ui.theme.primaryColor
import codeverse.brzodolokacije.utils.Validation

@Composable
fun AddTagDialog(
    onDismiss: () -> Unit,
    onConfirm: (tag: String) -> Unit,
    toastTagLocationSuccess: Toast,
    tagLocation: MutableList<String>
){

    var text by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = {
            onDismiss()
        },
        title = {
            Text(text = "Unesi tag")
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                })
                {
                    Text(text = "Odustani", color = primaryColor)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {

                val doesExist = tagLocation.find {
                    it.lowercase() == text.lowercase().trim()
                }

                if(text.isEmpty()){
                    Toast.makeText(context,"Ne možete uneti prazan tag",Toast.LENGTH_LONG).show()
                }
                else if (doesExist != null){
                    Toast.makeText(context,"${doesExist} je već dodat",Toast.LENGTH_LONG).show()
                }
                else if(!Validation.isLettersAndDigits(text)){
                    Toast.makeText(context,"Tag može sadržati samo slova i brojeve",Toast.LENGTH_LONG).show()
                }
                else {
                    onConfirm(text)
                    toastTagLocationSuccess.show()
                }
                }) {
                Text(text = "Dodaj", color = primaryColor)
            }
        },
        text = {
            Column() {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    label = { Text(text = "Tag") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {

                            val doesExist = tagLocation.find {
                                it.lowercase() == text.lowercase().trim()
                            }

                            if(text.isEmpty()){
                                Toast.makeText(context,"Ne možete uneti prazan tag",Toast.LENGTH_LONG).show()
                            }
                            else if (doesExist != null){
                                Toast.makeText(context,"${doesExist} je već dodat",Toast.LENGTH_LONG).show()
                            }
                            else if(!Validation.isLettersAndDigits(text)){
                                Toast.makeText(context,"Tag može sadržati samo slova i brojeve",Toast.LENGTH_LONG).show()
                            }
                            else {
                                onConfirm(text)
                                toastTagLocationSuccess.show()
                            }
                        }
                    )
                )
            }
        }
    )
}