package com.example.rodolfosarabo.testezendeskchatapi

import android.app.Application
import android.util.Base64
import zendesk.core.Zendesk
import zendesk.support.Support

class Global : Application() {

    override fun onCreate() {
        super.onCreate()

        Zendesk.INSTANCE.init(this, "https://companytestrodolfo.zendesk.com",
                "b690797aa26bbdcd09e847cdd5aa540c23f49ec30808c709",
                "mobile_sdk_client_381853b111de4f891bc1")
        Support.INSTANCE.init(Zendesk.INSTANCE)

        val byteArray = TOKEN.toByteArray(Charsets.UTF_8)
        val tmp = Base64.encodeToString(byteArray, Base64.NO_WRAP)
        Prefs(this).saveUserAuth(AGENT_AUTH, "Basic $tmp")

    }

}