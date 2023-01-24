package codeverse.brzodolokacije.utils

import android.content.Context
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions
import com.mapbox.mapboxsdk.utils.BitmapUtils
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Remembers a MapView and gives it the lifecycle of the current LifecycleOwner
 */
@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }

    // Makes MapView follow the lifecycle of this composable
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, mapView) {
        val lifecycleObserver = getMapLifecycleObserver(mapView)
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

private fun getMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE ->
            {
                mapView.onCreate(Bundle())
            }
            Lifecycle.Event.ON_START ->
            {
                println("DESIO SE START")
                mapView.onStart()
            }
            Lifecycle.Event.ON_RESUME -> mapView.onResume()
            Lifecycle.Event.ON_PAUSE -> mapView.onPause()
            Lifecycle.Event.ON_STOP -> mapView.onStop()
            Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
            else -> throw IllegalStateException()
        }
    }

suspend inline fun MapView.awaitMap(context: Context): MapboxMap =
    suspendCoroutine { continuation ->
        getMapAsync { map ->
            val mapTilerKey = Constants.MAPTILER_API_KEY
            val styleUrl = "https://api.maptiler.com/maps/openstreetmap/style.json?key=${mapTilerKey}";

            // Set the style after mapView was loaded
            map.setStyle(styleUrl) {
                map.uiSettings.setAttributionMargins(15, 0, 0, 15)

                addMarkerImageToStyle(it, context)

                }
                continuation.resume(map)
            }
        }

fun addMarkerImageToStyle(style: Style, context: Context) {
    style.addImage(
        "marker",
        BitmapUtils.getBitmapFromDrawable(context.getDrawable(com.mapbox.mapboxsdk.R.drawable.mapbox_marker_icon_default))!!,
        true
    )
}