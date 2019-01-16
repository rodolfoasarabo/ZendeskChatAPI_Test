package com.example.rodolfosarabo.testezendeskchatapi

import android.content.Context
import android.content.SharedPreferences
import com.example.rodolfosarabo.testezendeskchatapi.models.UserModel

class Prefs(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)

    fun saveUserAuth(key: String, auth: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, auth)
        editor.apply()
    }

    fun getUserAuth(key: String) = sharedPreferences.getString(key, "") ?: ""

    fun saveUserData(user: UserModel) {
        val editor = sharedPreferences.edit()
        editor.putLong(USER_ID, user.id ?: 0)
        editor.putString(NAME, user.name)
        editor.putString(EMAIL, user.email)
        editor.apply()
    }

    fun getUserData(): UserModel? {
        val id = sharedPreferences.getLong(USER_ID, 0)
        val name = sharedPreferences.getString(NAME, "")
        val email = sharedPreferences.getString(EMAIL, "")
        return UserModel(id = id, name = name, email = email)
    }

    fun clearUserData() {
        val editor = sharedPreferences.edit()
        editor.remove(USER_ID)
        editor.remove(NAME)
        editor.remove(EMAIL)
        editor.apply()
    }

}