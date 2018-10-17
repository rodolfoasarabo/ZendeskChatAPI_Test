package com.example.rodolfosarabo.testezendeskchatapi

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.zopim.android.sdk.api.Chat
import com.zopim.android.sdk.api.ZopimChat
import com.zopim.android.sdk.chatlog.ZopimChatLogFragment
import com.zopim.android.sdk.embeddable.ChatActions
import com.zopim.android.sdk.model.VisitorInfo
import com.zopim.android.sdk.prechat.ChatListener
import com.zopim.android.sdk.prechat.PreChatForm
import com.zopim.android.sdk.prechat.ZopimChatFragment
import com.zopim.android.sdk.widget.ChatWidgetService

class PreChatActivity : AppCompatActivity(), ChatListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_chat)

        if (savedInstanceState != null) {
            return
        }
    }

    override fun onResume() {
        super.onResume()
        val widgetWasActive: Boolean = stopService(Intent(this, ChatWidgetService::class.java))

        if (widgetWasActive) {
            resumeChat()
            return
        }

        if (intent != null) {
            val action = intent.action
            if (ChatActions.ACTION_RESUME_CHAT == action) {
                resumeChat()
                return
            }
        }

        val chat: Chat = ZopimChat.resume(this)
        if (!chat.hasEnded()) {
            resumeChat()
            return
        }

        run {
            val visitorInfo = VisitorInfo.Builder()
                    .name("Rodolfo")
                    .email("rodolfoasarabo@gmail.com").phoneNumber("15997354423").build()

            ZopimChat.setVisitorInfo(visitorInfo)

            val fragment = ZopimChatFragment()

            val manager = supportFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.chat_fragment_container, fragment, ZopimChatFragment::class.java.name)
            transaction.commit()

        }
    }

    private fun resumeChat() {
        val manager = supportFragmentManager
        if (manager.findFragmentByTag(ZopimChatFragment::class.java.name) == null) {
            val chatLogFragment = ZopimChatLogFragment()
            val transaction = manager.beginTransaction()
            transaction.add(com.zopim.android.sdk.R.id.chat_fragment_container, chatLogFragment, ZopimChatLogFragment::class.java.name)
            transaction.commit()
        }
    }

    override fun onChatLoaded(p0: Chat?) {
        //TODO
    }

    override fun onChatInitialized() {
        //TODO
    }

    override fun onChatEnded() {
        finish()
    }

}