package com.example.rodolfosarabo.testezendeskchatapi

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var chatButton: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, PreChatActivity::class.java)

        chatButton = findViewById(R.id.chat_button)

        chatButton.setOnClickListener {
            startActivity(intent)
        }

    }

}
