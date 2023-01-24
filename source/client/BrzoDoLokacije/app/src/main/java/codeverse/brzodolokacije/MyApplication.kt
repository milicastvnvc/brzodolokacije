package codeverse.brzodolokacije

import android.app.Application
import codeverse.brzodolokacije.utils.Constants
import dagger.hilt.android.HiltAndroidApp
import io.radar.sdk.Radar

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Radar.initialize(this, Constants.API_KEY_RADAR)
    }
}