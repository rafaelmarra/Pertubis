package com.example.rafaelmarra.pertubis.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rafaelmarra.pertubis.extensions.cancelAlarm
import com.example.rafaelmarra.pertubis.model.disturb.Disturb
import com.example.rafaelmarra.pertubis.model.disturb.DisturbDatabase

class ListaViewModel(private val mApplication: Application) : AndroidViewModel(mApplication) {

    private val disturbDatabase: DisturbDatabase? = DisturbDatabase.getInstance(mApplication)

    var disturbList: LiveData<List<Disturb>>

    init {
        disturbList = disturbDatabase?.disturbDao()?.getAll()!!  //é sucesso
    }

    fun getDisturbData(){
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

}