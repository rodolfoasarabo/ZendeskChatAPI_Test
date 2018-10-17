package com.example.rodolfosarabo.testezendeskchatapi.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.util.Log
import com.example.rodolfosarabo.testezendeskchatapi.PreChatActivity
import com.example.rodolfosarabo.testezendeskchatapi.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.zopim.android.sdk.api.ZopimChat
import com.zopim.android.sdk.api.ZopimChatApi
import com.zopim.android.sdk.embeddable.ChatActions
import com.zopim.android.sdk.model.PushData

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        const val TAG: String = "FBMessagingService"

        val NOTIFICATION_ID = System.currentTimeMillis().toInt()

        const val NOTIFICATION_CHANNEL_ID = "TestConstraintsChannelId"
        const val NOTIFICATION_CHANNEL_NAME = "TestConstraintsChannelId"
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Log.d("FCM", token)

        ZopimChat.setPushToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        Log.d(TAG, "Message Received")

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setAutoCancel(true)
        }

        val intent = Intent(this, PreChatActivity::class.java)

        intent.action = ChatActions.ACTION_RESUME_CHAT

        val pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        builder.setContentIntent(pendingIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 100, 200)

            builder.setChannelId(NOTIFICATION_CHANNEL_ID)
            manager.createNotificationChannel(channel)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.priority = Notification.PRIORITY_HIGH
        }

        val pushData: PushData = PushData.getChatNotification(message.data)

        when (pushData.type) {
            PushData.Type.END -> {
                builder.setContentTitle("Chat ended")
                builder.setContentText("The chat has ended!")
            }
            PushData.Type.MESSAGE -> {
                builder.setContentTitle("Chat message")
                builder.setContentText("New chat message!")
            }
            PushData.Type.NOT_CHAT -> {

            }
        }

        manager.notify(NOTIFICATION_ID, builder.build())

        ZopimChatApi.onMessageReceived(pushData)
    }

}