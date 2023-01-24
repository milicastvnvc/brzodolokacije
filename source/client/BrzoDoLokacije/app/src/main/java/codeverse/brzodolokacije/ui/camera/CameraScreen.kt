package codeverse.brzodolokacije.ui.camera

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.ui.theme.lightBackgroundColor
import coil.compose.rememberImagePainter
import java.io.File


@Composable
fun CameraScreen(
    navController: NavController,
    onImageSave: (Uri, File) -> Unit,
    closeCamera: () -> Unit
) {

    val emptyImageUri = Uri.parse("file://dev/null")
    var file by remember { mutableStateOf<File?>(null) }
    var imageUri by remember { mutableStateOf(emptyImageUri) }
    if (imageUri != emptyImageUri) {
        Box(modifier = Modifier) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = rememberImagePainter(imageUri),
                contentDescription = "Captured image",
                contentScale = ContentScale.Fit
            )
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = lightBackgroundColor,
                modifier = Modifier
                    .size(54.dp)
                    .clickable {
                        closeCamera()
                    }
                    .align(Alignment.TopStart)
                    .padding(10.dp)
                //.clip(shape = CircleShape)

            )
            //Spacer(modifier = Modifier.height(10.dp).align(Alignment.BottomCenter))
            Row(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                ,horizontalArrangement = Arrangement.SpaceAround
            ){
                Icon(
                    painter = painterResource(id = R.drawable.replay_40),//replay_circle_filled_40
                    contentDescription = null,
                    //contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(0.15f)
                        .aspectRatio(0.9f, matchHeightConstraintsFirst = false)
                        //.border(width = 0.dp, color = primaryColor, shape = CircleShape)
                        .padding(bottom = 15.dp)
                        .clip(CircleShape)
                        .clickable(onClick = {
                            imageUri = emptyImageUri
//
                        })
                )
                Image(
                    painter = painterResource(id = R.drawable.add_a_photo_40),//add_to_photos_40_outlined
                    contentDescription = null,
                    //contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(0.19f)
                        .aspectRatio(0.9f, matchHeightConstraintsFirst = false)
                        //.border(width = 0.dp, color = primaryColor, shape = CircleShape)
                        .padding(bottom = 15.dp)
                        .clip(CircleShape)
                        .clickable(onClick = {
                            onImageSave(imageUri, file!!)
                        })
                )

            }

        }
    } else {
        CameraCapture(
            navController,
            modifier = Modifier.fillMaxHeight(),
            onImageFile = { fil ->
                file = fil
                imageUri = fil.toUri()
            },
            closeCamera = closeCamera
        )
    }
}
