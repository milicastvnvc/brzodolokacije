package codeverse.brzodolokacije.utils.managers

import android.content.Context
import android.content.SharedPreferences
import codeverse.brzodolokacije.data.models.User
import codeverse.brzodolokacije.utils.Constants
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class UserManager @Inject constructor(@ApplicationContext context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(Constants.PREFS_USER, Context.MODE_PRIVATE)

    fun saveUser(user: User) {

        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        editor.putString(Constants.USER, json)
        editor.apply()
    }

    fun getUser(): User? {
        val gson = Gson()
        val user = gson.fromJson(prefs.getString(Constants.USER, null),User::class.java)
        return user
    }

    fun deleteUser() {
        // IMPLEMENTIRATI BRISANJE USERA!!!
        val editor = prefs.edit()
        editor.remove(Constants.USER)
        editor.apply()
    }
}