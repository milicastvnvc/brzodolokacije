package codeverse.brzodolokacije.ui.camera

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import codeverse.brzodolokacije.R
import codeverse.brzodolokacije.ui.camera.permission.Permission
import kotlinx.coroutines.launch
import java.io.File

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter

import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import codeverse.brzodolokacije.ui.camera.helpers.executor
import codeverse.brzodolokacije.ui.camera.helpers.getCameraProvider
import codeverse.brzodolokacije.ui.theme.lightBackgroundColor
import codeverse.brzodolokacije.ui.theme.primaryColor


@Composable
fun CameraCapture(
    navController: NavController,
    modifier: Modifier = Modifier,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    onImageFile: (File) -> Unit = { },
    closeCamera: () -> Unit
) {
    val context = LocalContext.current
    Permission(
        permission = Manifest.permission.CAMERA,
        rationale = "Da bi koristio kameru, moraš da nam daš dozvolu za korišćenje kamere.",
        permissionNotAvailableContent = {
            Row(modifier.fillMaxWidth().padding(10.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Top){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = lightBackgroundColor,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            navController.navigateUp()
                        }
                )
            }
            Column(modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {

                Text("Nemate dozvolu za korišćenje kamere.")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    })
                }) {
                    Text("Izmeni u podešavanjima")
                }
            }
        }
    ) {
        Box(modifier = modifier) {
            val lifecycleOwner = LocalLifecycleOwner.current
            val coroutineScope = rememberCoroutineScope()
            var previewUseCase by remember { mutableStateOf<UseCase>(Preview.Builder().build()) }
            val imageCaptureUseCase by remember {
                mutableStateOf(
                    ImageCapture.Builder()
                        .setCaptureMode(CAPTURE_MODE_MAXIMIZE_QUALITY)
                        .build()
                )
            }
            Box {
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onUseCase = {
                        previewUseCase = it
                    }
                )
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = lightBackgroundColor,
                    modifier = Modifier
                        .size(54.dp)
                        .clickable {
                            //vrati se nazad pomocu neke funkcije
                            closeCamera()
                        }
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                    //.clip(shape = CircleShape)

                )
//                Image(
//                    painter = painterResource(id = R.drawable.arrow_circle_up),
//                    contentDescription = null,
//                    //contentScale = ContentScale.Crop,
//                    modifier = Modifier
//                        .fillMaxWidth(0.15f)
//                        .aspectRatio(0.9f, matchHeightConstraintsFirst = false)
//                        //.border(width = 0.dp, color = primaryColor, shape = CircleShape)
//                        .padding(start = 15.dp)
//                        .clip(CircleShape)
//                        .clickable(onClick = {
//                                //navController.navigateUp()
//                        })
//                        .align(Alignment.TopStart),
//                )
                Image(
                    painter = painterResource(id = R.drawable.ic_camera),
                    contentDescription = null,
                    //contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth(0.22f)
                        .aspectRatio(0.9f, matchHeightConstraintsFirst = false)
                        //.border(width = 0.dp, color = primaryColor, shape = CircleShape)
                        .padding(bottom = 15.dp)
                        .clip(CircleShape)
                        .clickable(onClick = {
                            coroutineScope.launch {
                                imageCaptureUseCase
                                    .takePicture(context.executor)
                                    .let {
                                        onImageFile(it)
                                    }
                            }
                        })
                        .align(Alignment.BottomCenter),
                )
            }
            LaunchedEffect(previewUseCase) {
                val cameraProvider = context.getCameraProvider()
                try {
                    // Must unbind the use-cases before rebinding them.
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, previewUseCase, imageCaptureUseCase
                    )
                } catch (ex: Exception) {
                    println("TakePicture " + "Failed to bind camera use cases")
                    ex.printStackTrace()
                }
            }
        }
    }
}

@Composable
fun TakePhotoImage(
    modifier: Modifier = Modifier,
    profileImage: Painter
) {
    Image(
        painter = profileImage,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(width = 2.dp, color = primaryColor, shape = CircleShape)
            .padding(3.dp)
            .clip(CircleShape)
            .clickable { }
    )

}