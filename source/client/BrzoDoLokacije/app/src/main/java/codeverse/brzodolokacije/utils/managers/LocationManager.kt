package codeverse.brzodolokacije.utils.managers

import android.content.Context
import android.content.SharedPreferences
import codeverse.brzodolokacije.utils.Constants
import com.google.gson.Gson
import com.mapbox.mapboxsdk.geometry.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationManager @Inject constructor(@ApplicationContext context : Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(Constants.PREFS_LOCATION, Context.MODE_PRIVATE)

    fun saveLocation(location: LatLng) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(location)

        editor.putString(Constants.LOCATION, json)

        editor.apply()
    }

    fun getLocation(): LatLng? {
        val gson = Gson()
        val location = gson.fromJson(prefs.getString(Constants.LOCATION, null), LatLng::class.java)
        return location
    }

    fun deleteLocation() {
        val editor = prefs.edit()

        editor.remove(Constants.LOCATION)

        editor.apply()
    }
}