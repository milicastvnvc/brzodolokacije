package codeverse.brzodolokacije

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import codeverse.brzodolokacije.domain.use_case.auth.RefreshTokenUseCase
import codeverse.brzodolokacije.ui.theme.BrzoDoLokacijeTheme
import codeverse.brzodolokacije.utils.Result
import codeverse.brzodolokacije.utils.managers.LocationManager
import codeverse.brzodolokacije.utils.managers.TokenManager
import codeverse.brzodolokacije.utils.managers.UserManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.geometry.LatLng
import dagger.hilt.android.AndroidEntryPoint
import io.radar.sdk.Radar
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var tokenManager: TokenManager
    @Inject
    lateinit var userManager: UserManager
    @Inject
    lateinit var locationManager: LocationManager

    @Inject
    lateinit var refreshTokenUseCase: RefreshTokenUseCase

    lateinit var refreshCoroutine: Job

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Mapbox.getInstance(this, null)

        val permissionsStorage = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        val requestExternalStorage = 1
        val permission =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionsStorage, requestExternalStorage)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION), 0)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 0)
        }
        if (tokenManager.getToken() == null){
            startActivity(Intent(this, LoginActivity::class.java))
        }
        else{

            Radar.getLocation { status, location, stopped ->
                // do something with location
                if(location != null){
                    println("Koordinate: " + location!!.latitude + " " + location!!.longitude)
                    locationManager.saveLocation(LatLng(location!!.latitude,location!!.longitude))
                }

            }

            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            //WindowCompat.setDecorFitsSystemWindows(window, false)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            setContent {
                BrzoDoLokacijeTheme {
                    MainScreen()
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()

        //println("START OF ACTIVITY")
        refreshCoroutine = refreshTokenUseCase(30.minutes).onEach { result ->
            when(result){
                is Result.Error -> {
                    //Toast.makeText(this,result.message,Toast.LENGTH_LONG).show()

                    if (result.redirect) {
                        tokenManager.deleteToken()
                        tokenManager.deleteToken(isRefresh = true)
                        userManager.deleteUser()
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                }
            }
        }.launchIn(lifecycleScope)
    }

    override fun onStop() {
        super.onStop()

        //println("STOP OF ACTIVITY")
        refreshCoroutine.cancel()
    }
}
