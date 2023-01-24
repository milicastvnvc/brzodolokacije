package codeverse.brzodolokacije.utils.managers

import android.content.Context
import android.content.SharedPreferences
import codeverse.brzodolokacije.data.models.maps.input.Feature
import codeverse.brzodolokacije.utils.Constants
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class FeatureManager @Inject constructor(@ApplicationContext context : Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(Constants.PREFS_FEATURE, Context.MODE_PRIVATE)

    fun saveFeature(feature: Feature) {

        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(feature)
        editor.putString(Constants.FEATURE, json)
        editor.apply()
    }

    fun getFeature(): Feature? {
        val gson = Gson()
        val feature = gson.fromJson(prefs.getString(Constants.FEATURE, null), Feature::class.java)
        return feature
    }

    fun deleteFeature() {
        val editor = prefs.edit()
        editor.remove(Constants.FEATURE)
        editor.apply()
    }

}