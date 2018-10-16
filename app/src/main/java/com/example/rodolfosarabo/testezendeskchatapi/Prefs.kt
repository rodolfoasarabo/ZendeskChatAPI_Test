package com.example.rodolfosarabo.testezendeskchatapi

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context){

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)

    fun saveUserData(user: User){
        val editor = sharedPreferences.edit()
        editor.putString(NAME, user.name)
        editor.putString(EMAIL, user.email)
        editor.putString(PHONE, user.phone)
        editor.apply()
    }

    fun getUserData(): User {
        val user = User()
        user.name = sharedPreferences.getString(NAME, "") ?: ""
        user.email = sharedPreferences.getString(EMAIL, "") ?: ""
        user.phone = sharedPreferences.getString(PHONE, "") ?: ""

        return user
    }

    fun clearUserData() {
        sharedPreferences.edit().clear().apply()
    }

}