package codeverse.brzodolokacije.ui.search.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import codeverse.brzodolokacije.ui.theme.lightBackgroundColor
import codeverse.brzodolokacije.ui.theme.primaryColor
import codeverse.brzodolokacije.ui.theme.secondaryColor

@Composable
fun SearchBar(searchFieldState: MutableState<String>, searchPosts: () -> Unit, text: String) {
    Row(
        Modifier
            .padding(horizontal = 22.dp)
            .height(54.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TextField(
            value = searchFieldState.value,
            onValueChange = { value ->
                searchFieldState.value = value
            },
            label = {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    color = secondaryColor.copy(0.8f),
                    fontWeight = FontWeight(400)
                )
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    searchPosts()
                }
            ),
            textStyle = TextStyle(color = secondaryColor, fontWeight = FontWeight(500), fontSize = 16.sp),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search, // Icons.Default.Search,
                    contentDescription = "Search",
                    tint = primaryColor,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(24.dp)
                )
            },
            trailingIcon = {
                if (searchFieldState.value != "") {
                    IconButton(
                        onClick = {
                            searchFieldState.value = ""
                            searchPosts()
                        }
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(12.dp)
                                .size(24.dp),
                            tint = primaryColor
                        )
                    }
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = secondaryColor,
                cursorColor = secondaryColor,
                leadingIconColor = secondaryColor,
                backgroundColor = lightBackgroundColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .weight(0.3f)
                .fillMaxHeight()
        )
//        Spacer(modifier = Modifier.width(8.dp))
//        IconButton(onClick = { }) {
//            Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "", tint = secondaryColor)
//        }
//        IconButton(onClick = {}) {
//            Icon(imageVector = Icons.Outlined.Notifications, contentDescription = "", tint = secondaryColor)
//        }
    }
}