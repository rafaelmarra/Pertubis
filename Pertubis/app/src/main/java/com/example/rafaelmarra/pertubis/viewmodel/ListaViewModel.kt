package com.example.rafaelmarra.pertubis.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rafaelmarra.pertubis.extensions.cancelAlarm
import com.example.rafaelmarra.pertubis.extensions.formatTime
import com.example.rafaelmarra.pertubis.model.disturb.Disturb
import com.example.rafaelmarra.pertubis.model.disturb.DisturbDatabase

class ListaViewModel(private val mApplication: Application) : AndroidViewModel(mApplication) {

    private val disturbDatabase: DisturbDatabase? = DisturbDatabase.getInstance(mApplication)

    var disturbList: LiveData<List<Disturb>>

    private val prefs = mApplication.getSharedPreferences(PREFS_FILENAME, 0)
    private val selectedOption by lazy { prefs.getInt(PREFS_SELECTED, RADIO_NEVER) }

    init {
        disturbList = disturbDatabase?.disturbDao()?.getAll()!!
    }

    fun getDisturbData() {
        disturbList = disturbDatabase?.disturbDao()?.getAll()!!
    }

    fun deleteDisturb(disturb: Disturb) {

        val intentCode = disturb.intentCode
        if (intentCode != null) {
            cancelAlarm(mApplication, intentCode)
        }

        val task = Runnable {
            disturbDatabase?.disturbDao()?.delete(disturb)
        }
        Thread(task).start()
    }

    fun getClearList(dirtyList: List<Disturb>): List<Disturb> {

        when (selectedOption) {

            RADIO_NEVER -> return dirtyList

            RADIO_NOW -> return getClearListOperation(dirtyList, 0)

            RADIO_HOUR -> return getClearListOperation(dirtyList, 60 * 60 * 1000)

            RADIO_DAY -> return getClearListOperation(dirtyList, 24 * 60 * 60 * 1000)

            RADIO_WEEK -> return getClearListOperation(dirtyList, 7 * 24 * 60 * 60 * 1000)
        }
        return dirtyList
    }

    private fun getClearListOperation(dirtyList: List<Disturb>, timeInterval: Long): List<Disturb> {
        val cleanList = mutableListOf<Disturb>()
        for (disturb in dirtyList) {
            val disturbTime = formatTime(disturb.hora!!, disturb.data!!)
            if (disturbTime + timeInterval > System.currentTimeMillis()) {
                cleanList.add(disturb)
            } else {
                deleteDisturb(disturb)
            }
        }
        return cleanList
    }
}