package codeverse.brzodolokacije.ui.components

import android.inputmethodservice.Keyboard.KEYCODE_DONE
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import codeverse.brzodolokacije.data.models.Location
import codeverse.brzodolokacije.data.models.maps.input.Feature
import codeverse.brzodolokacije.ui.theme.backgroundColor
import codeverse.brzodolokacije.ui.theme.primaryColor


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchField(state: MutableState<TextFieldValue>,
                onSearch : (text: String) -> Unit){

    var isExpanded = remember { mutableStateOf(false) }
    var locations : MutableList<Feature> = mutableListOf<Feature>()
    var mTextFieldSize = remember { mutableStateOf(Size.Zero)}
    

//    if(state.success){
//        if(state.data != null) {
//            locations = state.data
//        }
//    }
//
//    if(addState.error.isNotBlank()){
//        //nadji gde da ispises gresku
//    }

    // Up Icon when expanded and down icon when collapsed
    val icon = if (isExpanded.value)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                println(state.value.text)
                onSearch(state.value.text)
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                // This value is used to assign to
                // the DropDown the same width
                mTextFieldSize.value = coordinates.size.toSize()
            },
        textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = CircleShape,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            cursorColor = Color.Black,
            leadingIconColor = primaryColor,
            trailingIconColor = primaryColor,
            backgroundColor = backgroundColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
    DropdownMenu(
        expanded = isExpanded.value,
        onDismissRequest = { isExpanded.value = false },
        modifier = Modifier
            .width(with(LocalDensity.current){ mTextFieldSize.value.width.toDp()})
    ) {
        locations.forEach { location ->
            DropdownMenuItem(onClick = {
                //prikazi koordinate
                isExpanded.value = false
            }) {
                Text(text = location.place_name_sr)
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SearchFieldPreview() {
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    //SearchField(textState,test)
}