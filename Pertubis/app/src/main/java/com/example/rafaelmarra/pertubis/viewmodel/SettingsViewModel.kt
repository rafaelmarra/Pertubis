package com.example.rafaelmarra.pertubis.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.rafaelmarra.pertubis.R

const val PREFS_FILENAME = "com.example.rafaelmarra.pertubis.prefs"
const val PREFS_SELECTED = "selected_option"
const val RADIO_NOW = R.id.radioButtonNow
const val RADIO_HOUR = R.id.radioButtonHour
const val RADIO_DAY = R.id.radioButtonDay
const val RADIO_WEEK = R.id.radioButtonWeek
const val RADIO_NEVER = R.id.radioButtonNever

class SettingsViewModel(private val mApplication: Application) : AndroidViewModel(mApplication) {

    private val prefs = mApplication.getSharedPreferences(PREFS_FILENAME, 0)
    private val prefsEditor = prefs.edit()
    private val selectedOption = prefs.getInt(PREFS_SELECTED, RADIO_NEVER)

    fun getSelectedRadioOption(): Int {
        return selectedOption
    }

    fun saveSelectedId(id: Int) {
        prefsEditor.putInt(PREFS_SELECTED, id)
        prefsEditor.apply()
    }
}