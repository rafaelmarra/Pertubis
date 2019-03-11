package com.example.rafaelmarra.pertubis.viewmodel.business

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

const val CHANNEL_ID = "channel-id"
const val NOTIFICATION_ID = "notification-id"
const val NOTIFICATION = "notification"

class NotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = intent?.getParcelableExtra<Notification>(NOTIFICATION)
        val id = intent?.getIntExtra(NOTIFICATION_ID, 0) ?: 0

        notificationManager.notify(id, notification)
    }


}