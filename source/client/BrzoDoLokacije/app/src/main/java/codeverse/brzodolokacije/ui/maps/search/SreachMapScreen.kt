package codeverse.brzodolokacije.ui.maps.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import codeverse.brzodolokacije.data.models.maps.input.Feature
import codeverse.brzodolokacije.ui.theme.backgroundColor
import codeverse.brzodolokacije.ui.theme.lightBackgroundColor
import codeverse.brzodolokacije.ui.theme.primaryColor
import com.mapbox.mapboxsdk.maps.MapboxMap

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchMapScreen(setLocation: (feature: Feature) -> Unit,
                    searchMapViewModel: SearchMapViewModel = hiltViewModel()) {

    var isExpanded = remember { mutableStateOf(false) }
    var locations: MutableList<Feature> = mutableListOf<Feature>()
    var mTextFieldSize = remember { mutableStateOf(Size.Zero) }
    val searchFieldState = remember { mutableStateOf("") }
    val inputPlaceState = searchMapViewModel.inputPlaceState.value


    if (inputPlaceState.success) {
        if (inputPlaceState.data != null) {
            locations = inputPlaceState.data.features
        }
    }
//
//    if(addState.error.isNotBlank()){
//        //nadji gde da ispises gresku
//    }

    // Up Icon when expanded and down icon when collapsed
    val icon = if (isExpanded.value)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown


    OutlinedTextField(
        value = searchFieldState.value,
        onValueChange = { value ->
            searchFieldState.value = value
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                searchMapViewModel.onInputSearch(searchFieldState.value)
                isExpanded.value = true
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned { coordinates ->
                // This value is used to assign to
                // the DropDown the same width
                mTextFieldSize.value = coordinates.size.toSize()
            },
        textStyle = TextStyle(color = lightBackgroundColor, fontSize = 18.sp),
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
            if(locations.size != 0){
                Icon(icon,"contentDescription",
                    Modifier.clickable { isExpanded.value = !isExpanded.value })
            }
            else{
                if (searchFieldState.value != "") {
                    IconButton(
                        onClick = {
                            searchFieldState.value = "" // Remove text from TextField when you press the 'X' icon
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
            }
        },
        singleLine = true,
        shape = CircleShape,
        colors = TextFieldDefaults.textFieldColors(
            textColor = lightBackgroundColor,
            cursorColor = lightBackgroundColor,
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
            .width(with(LocalDensity.current) { mTextFieldSize.value.width.toDp() })
    ) {
        locations.forEach { location ->
            DropdownMenuItem(
                onClick = {
                //setuj marker
                setLocation(location)
                isExpanded.value = false
                searchFieldState.value = location.place_name
            }) {
                Text(text = location.place_name)
            }
        }
    }
}