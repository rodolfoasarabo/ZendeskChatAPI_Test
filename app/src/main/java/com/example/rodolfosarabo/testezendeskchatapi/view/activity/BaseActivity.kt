package com.example.rodolfosarabo.testezendeskchatapi.view.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.rodolfosarabo.testezendeskchatapi.Prefs
import com.example.rodolfosarabo.testezendeskchatapi.retrofit.RetrofitInitializer
import zendesk.core.Zendesk
import zendesk.support.Support
import zendesk.core.AnonymousIdentity



open class BaseActivity : AppCompatActivity() {

    internal lateinit var prefs: Prefs
    val apontamentos = RetrofitInitializer().ticketsService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = Prefs(this)

    }

}