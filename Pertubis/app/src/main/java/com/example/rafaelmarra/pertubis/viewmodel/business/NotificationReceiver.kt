package com.example.rafaelmarra.pertubis.viewmodel.business

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.rafaelmarra.pertubis.R
import com.example.rafaelmarra.pertubis.view.ListaActivity

const val CHANNEL_ID = "channel-id"
const val NOTIFICATION_ID = "notification-id"

class NotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val disturbed = intent?.extras?.getString("disturbed") ?: "Ninguém"
        val disturb = intent?.extras?.getString("disturb") ?: "Nada"

        val notification = createNotification(context, disturbed, disturb)
        val id = intent?.getIntExtra(NOTIFICATION_ID, 0) ?: 0

        notificationManager.notify(id, notification)
    }

    private fun createNotification(context: Context, disturbed: String, disturb: String): Notification? {

        val intent = Intent(context, ListaActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_large)
            .setContentTitle("Não esqueça de perturbar ${if (disturbed == "Rosana") {
                "a"
            } else {
                "o"
            }} $disturbed!")
            .setContentText(disturb
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        return builder.build()
    }
}