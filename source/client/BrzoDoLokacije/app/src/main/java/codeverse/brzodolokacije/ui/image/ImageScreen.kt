package codeverse.brzodolokacije.ui.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import codeverse.brzodolokacije.ui.theme.backgroundColor
import coil.compose.rememberImagePainter

@Composable
fun ImageScreen(navController: NavController, imageViewModel: ImageViewModel = hiltViewModel()){

    val image = remember { imageViewModel.resource }
    val scale = remember { mutableStateOf(1f) }
//    var translation by remember { mutableStateOf(Offset(0f, 0f)) }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center){
        image.value.let {
            Box(
                modifier = Modifier
                    .clip(RectangleShape) // Clip the box content
                    .fillMaxSize() // Give the size you want...
                    .background(backgroundColor)
                    .pointerInput(Unit) {
                        detectTransformGestures { centroid, pan, zoom, rotation ->
                            scale.value *= zoom
                        }
                    }
            ) {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(30.dp)
                        .align(Alignment.TopStart),
                    backgroundColor = backgroundColor,
                    onClick = {
                        navController.navigateUp()
                    }) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = "Close") }
                Image(
                    modifier = Modifier.fillMaxWidth()
                        .align(Alignment.Center)
                        // keep the image centralized into the Box
                        .graphicsLayer(
                            // adding some zoom limits (min 50%, max 200%)
                            scaleX = maxOf(1f, minOf(3f, scale.value)),
                            scaleY = maxOf(1f, minOf(3f, scale.value))
                        ),
                    contentDescription = "Slika",
                    painter = rememberImagePainter(image.value),
                    contentScale = ContentScale.FillWidth)
            }

        }
    }

}