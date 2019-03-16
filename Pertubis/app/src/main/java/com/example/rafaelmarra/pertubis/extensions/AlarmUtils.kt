package com.example.rafaelmarra.pertubis.extensions

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.rafaelmarra.pertubis.viewmodel.business.NotificationReceiver
import java.util.*

fun formatTime(time: String, day: String): Long {

    val minuteToSet = time.substring(3..4).toInt()
    val hourToSet = time.substring(0..1).toInt()
    val dayToSet = day.substring(0..1).toInt()
    val monthToSet = day.substring(3..4).toInt()
    val yearToSet = day.substring(6..9).toInt()

    val calendar: Calendar = Calendar.getInstance().apply {
        timeInMillis = System.currentTimeMillis()
        set(Calendar.MINUTE, minuteToSet)
        set(Calendar.HOUR_OF_DAY, hourToSet)
        set(Calendar.DAY_OF_MONTH, dayToSet)
        set(Calendar.MONTH, monthToSet - 1)
        set(Calendar.YEAR, yearToSet)
    }

 return calendar.timeInMillis
}

fun createAlarmWithCode(context: Context, disturbed: String, disturb: String, time: Long): Int {

    val bundle = Bundle().apply {
        putString("disturb", disturb)
        putString("disturbed", disturbed)
    }

    val notificationIntent = Intent(context, NotificationReceiver::class.java).apply {
        putExtras(bundle)
    }

    val requestCode = System.currentTimeMillis().toInt()

    val alarmIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        notificationIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.set(AlarmManager.RTC_WAKEUP, time, alarmIntent)

    return requestCode
}

fun cancelAlarm(context: Context, requestCode: Int) {

    val notificationIntent = Intent(context, NotificationReceiver::class.java)

    val alarmIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        notificationIntent,
        PendingIntent.FLAG_CANCEL_CURRENT
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(alarmIntent)
}