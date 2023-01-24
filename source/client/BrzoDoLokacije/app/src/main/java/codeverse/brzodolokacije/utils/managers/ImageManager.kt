package codeverse.brzodolokacije.utils.managers

import android.content.Context
import android.content.SharedPreferences
import codeverse.brzodolokacije.data.models.User
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ImageManager @Inject constructor(@ApplicationContext context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences("prefs_image", Context.MODE_PRIVATE)

    fun saveImage(image: String) {

        val editor = prefs.edit()
        editor.putString("image", image)
        editor.apply()
    }

    fun getImage(): String? {
        return prefs.getString("image", null)
    }

    fun deleteImage() {
        val editor = prefs.edit()
        editor.remove("image")
        editor.apply()
    }
}