package codeverse.brzodolokacije.ui.video

import android.graphics.drawable.Icon
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.ui.PlayerView

@Composable
fun VideoScreen(videoViewModel: VideoViewModel = hiltViewModel()){

    val videoItems by videoViewModel.videoItems.collectAsState()
    val selectVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let(videoViewModel::addVideoUri)
        })

    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner){
        val observer = LifecycleEventObserver { _, event ->
                lifecycle = event
            }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
      AndroidView(
          factory ={ context ->
              PlayerView(context).also{
                  it.player = videoViewModel.player

              }
          },
          update = {
            when(lifecycle){
                Lifecycle.Event.ON_PAUSE -> {
                    it.onPause()
                    it.player?.pause()
                }
                Lifecycle.Event.ON_RESUME -> {
                    it.onResume()
                }
                else -> Unit
            }
          },
          modifier = Modifier
              .fillMaxWidth()
              .aspectRatio(16 / 9f)
      )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            selectVideoLauncher.launch("video/mp4")
        }){
            Text(text="Izaberi video iz galerije")
        }
//        IconButton(onClick = {
//            selectVideoLauncher.launch("video/mp4")
//        }) {
//            Icon(imageVector = Icons.Default.FileUpload,
//                contentDescription = "Izaberi video")
//        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.fillMaxWidth()){
            items(videoItems) { item ->
                Text(
                    text = "video",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            videoViewModel.playVideo(item.contentUri)
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}