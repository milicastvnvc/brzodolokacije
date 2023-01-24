package codeverse.brzodolokacije

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import codeverse.brzodolokacije.ui.theme.backgroundColor
import codeverse.brzodolokacije.ui.theme.secondaryColor
import coil.compose.rememberImagePainter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraActivity : ComponentActivity() {
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private var shouldShowCamera: MutableState<Boolean> = mutableStateOf(false)
    private lateinit var photoUri: Uri
    private var shouldShowPhoto: MutableState<Boolean> = mutableStateOf(false)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.i("kilo", "Permission granted")
            shouldShowCamera.value = true
        } else {
            Log.i("kilo", "Permission denied")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (shouldShowCamera.value) {
//                CameraView(
//                    outputDirectory = outputDirectory,
//                    executor = cameraExecutor,
//                    onImageCaptured = ::handleImageCapture,
//                    onError = { Log.e("kilo", "View error:", it) }
//                )
            }
            if (shouldShowPhoto.value) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                ){



                    Image(
                        painter = rememberImagePainter(photoUri),
                        contentDescription = null,
//                        modifier = Modifier.fillMaxSize()
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    navigationButton(photoUri)
                }
            }
        }
        requestCameraPermission()

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("kilo", "Permission previously granted")
                shouldShowCamera.value = true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> Log.i("kilo", "Show camera permissions dialog")

            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
    private fun handleImageCapture(uri: Uri) {
        Log.i("kilo", "Image captured: $uri")
        shouldShowCamera.value = false
        photoUri = uri
        shouldShowPhoto.value = true
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
    @Composable
    private fun navigationButton(photoUri: Uri) {
        val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri)
            Button(
                onClick = {
                    val bitmapPhoto = MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
                    val isSavedSuccessfully = savePhototoInternalStorage("cameraPhoto", bitmapPhoto)
//                    val isSavedSuccessfullyUri = savePhototoInternalStorageUri(UUID.randomUUID().toString(), photoUri)
                    if(isSavedSuccessfully)
                        Toast.makeText(this@CameraActivity, "Uspesno je sacuvana fotografija",Toast.LENGTH_SHORT).show()
                    val bStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream)
                    val byteArray: ByteArray = bStream.toByteArray()

                    val anotherIntent = Intent(this@CameraActivity, MainActivity::class.java)
                    anotherIntent.putExtra("image", byteArray)
                    startActivity(anotherIntent)
                    finish()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = secondaryColor,
                    contentColor = backgroundColor
                ),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text(text = "Sledece", fontSize = 20.sp)

            }
            Spacer(modifier = Modifier.padding(10.dp))
            Button(
                onClick = {
                    this@CameraActivity.startActivity(Intent(this@CameraActivity, CameraActivity::class.java))
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = secondaryColor,
                    contentColor = backgroundColor
                ),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text(text = "Pokusaj opet", fontSize = 20.sp)

            }
    }

    //    private suspend fun loadPhotoFromInternalStorage() : List<InternalStoragePhoto> {
//        return withContext(Dispatchers.IO) {
//            val files = filesDir.listFiles()
//            files.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") }.map {
//                val bytes = it.readBytes()
//                val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                InternalStoragePhoto(it.name, bmp)
//            } ?: listOf()
//        }
//    }
    private fun savePhototoInternalStorage(filename: String, bmp: Bitmap): Boolean{
        return try {
            openFileOutput("$filename.jpg", MODE_PRIVATE).use { stream ->
                if(!bmp.compress(Bitmap.CompressFormat.JPEG,95, stream)) {
                    throw IOException("Ne moze da se sacuva bitmap")
                }

                true
            }

        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }


}

