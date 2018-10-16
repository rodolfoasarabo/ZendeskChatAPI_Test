package com.example.rodolfosarabo.testezendeskchatapi

import android.app.Application
import com.zopim.android.sdk.api.ZopimChat

class Global : Application() {

    override fun onCreate() {
        super.onCreate()

        ZopimChat.init(accountKey)
    }

}