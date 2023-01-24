package codeverse.brzodolokacije.utils.managers

import android.content.Context
import android.content.SharedPreferences
import codeverse.brzodolokacije.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context : Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(Constants.PREFS_AUTH_TOKEN, Context.MODE_PRIVATE)

    fun saveToken(token: String, isRefresh: Boolean = false) {
        val editor = prefs.edit()
        if (!isRefresh)
            editor.putString(Constants.AUTH_TOKEN, token)
        else
            editor.putString(Constants.REFRESH_TOKEN, token)
        editor.apply()
    }

    fun getToken(isRefresh: Boolean = false): String? {
        if (!isRefresh){
            return prefs.getString(Constants.AUTH_TOKEN, null)
        }
        return prefs.getString(Constants.REFRESH_TOKEN, null)
    }

    fun deleteToken(isRefresh: Boolean = false) {
        // IMPLEMENTIRATI BRISANJE TOKENA!!!
        val editor = prefs.edit()
        if(!isRefresh)
            editor.remove(Constants.AUTH_TOKEN)
        else
            editor.remove(Constants.REFRESH_TOKEN)
        editor.apply()
    }

}