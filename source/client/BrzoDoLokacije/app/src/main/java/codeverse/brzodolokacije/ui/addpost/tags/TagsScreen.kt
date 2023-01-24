package codeverse.brzodolokacije.ui.addpost.tags

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import codeverse.brzodolokacije.ui.theme.*
import codeverse.brzodolokacije.utils.Routes
import coil.compose.rememberImagePainter

@Composable
fun TagsScreen(
    navController: NavController,
    tagLocation: MutableList<String>,
    deleteTag: (tag: String) -> Unit,
    isEditable: Boolean = true
) {
    var items = tagLocation
    LazyRow(
        modifier = Modifier
            .padding(top = 10.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(0.dp)),
        contentPadding = PaddingValues(horizontal = 10.dp),
        horizontalArrangement = Arrangement.Start,
        state = rememberLazyListState()
    ) {
        itemsIndexed(items) { index, item ->
            Row {
                Row(
                    modifier = Modifier
                        .background(color = primaryColor, shape = RoundedCornerShape(5.dp))
                        .padding(vertical = 7.dp, horizontal = 10.dp),
                    horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        item,
                        modifier = Modifier
                            .clickable {
                                if (!isEditable) {
                                    navController.navigate(Routes.SEARCH + "/" + item)
                                }
                            },
                        color = lightBackgroundColor
                    )
                    Spacer(modifier = Modifier.width(10.dp))

                    if (isEditable) {
                        FloatingActionButton(
                            modifier = Modifier
                                .wrapContentSize(Alignment.TopEnd)
                                //.padding(all = 5.dp)
                                .size(20.dp),
                            backgroundColor = primaryColorLight,
                            onClick = {
                                deleteTag(item)
                            }) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Close",
                                tint = lightBackgroundColor
                            )

                        }
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
            }
            
        }
    }
//    ChipVerticalGrid(
//        spacing = 7.dp,
//        modifier = Modifier
//            .padding(7.dp)
//            .background(color = backgroundColor, shape = RoundedCornerShape(2))
//
//    ) {
//        items.forEach { item ->
//            Text(
//                item,
//                modifier = Modifier
//                    .background(color = (primaryColor.copy(.75f)), shape = CircleShape)
//                    .padding(vertical = 5.dp, horizontal = 10.dp),
//                color = lightBackgroundColor
//            )
//
//            if(isEditable){
//                FloatingActionButton(
//                    modifier = Modifier
//                        .wrapContentSize(Alignment.TopEnd)
//                        .padding(all = 5.dp)
//                        .size(20.dp),
//                    backgroundColor = backgroundColor,
//                    onClick = {
//
////                    items = tagLocation.filter { it != item }?.toMutableList()
////                    tagLocation.remove(item)
//                        deleteTag(item);
//                        println("Klikno sam na dugme")
//                    }) {
//                    Icon(
//                        imageVector = Icons.Outlined.Close,
//                        contentDescription = "Close"
//                    )
//
//                }
//            }


//            IconButton(
//                modifier = Modifier
//                    .wrapContentSize(Alignment.TopEnd)
//                    .padding(all = 5.dp)
//                    .size(20.dp),
//                onClick = {
//                    deleteTag(item);
//                    println("Klikno sam na dugme")
//                }
//            ) {
//                Icon(
//                    imageVector = Icons.Outlined.Close,
//                    contentDescription = "Close"
//                )
//            }
//        }
//    }

}

@Composable
fun ChipVerticalGrid(
    modifier: Modifier = Modifier,
    spacing: Dp,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        var currentRow = 0
        var currentOrigin = IntOffset.Zero
        val spacingValue = spacing.toPx().toInt()
        val placeables = measurables.map { measurable ->
            val placeable = measurable.measure(constraints)

            if (currentOrigin.x > 0f && currentOrigin.x + placeable.width > constraints.maxWidth) {
                currentRow += 1
                currentOrigin =
                    currentOrigin.copy(x = 0, y = currentOrigin.y + placeable.height + spacingValue)
            }

            placeable to currentOrigin.also {
                currentOrigin = it.copy(x = it.x + placeable.width + spacingValue)
            }
        }

        layout(
            width = constraints.maxWidth,
            height = placeables.lastOrNull()?.run { first.height + second.y } ?: 0
        ) {
            placeables.forEach {
                val (placeable, origin) = it
                placeable.place(origin.x, origin.y)
            }
        }
    }
}
