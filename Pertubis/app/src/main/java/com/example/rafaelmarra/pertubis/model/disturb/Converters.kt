package com.example.rafaelmarra.pertubis.model.disturb

import android.app.PendingIntent
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {

    @TypeConverter
    fun intentToString(pendingIntent: PendingIntent): String {
        return Gson().toJson(pendingIntent)
    }

    @TypeConverter
    fun stringToIntent(string: String): PendingIntent {
        val type = object : TypeToken<PendingIntent>() {}.type as Type
        return Gson().fromJson(string, type)
    }
}