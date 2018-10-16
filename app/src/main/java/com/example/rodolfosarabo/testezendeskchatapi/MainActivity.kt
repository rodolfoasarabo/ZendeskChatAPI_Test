package com.example.rodolfosarabo.testezendeskchatapi

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button


class MainActivity : AppCompatActivity() {

    private lateinit var chatButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chatButton = findViewById(R.id.chat_button)

        chatButton.setOnClickListener {
            startActivity(Intent(this, PreChatActivity::class.java))
        }

    }

}
