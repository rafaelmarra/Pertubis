package com.example.rafaelmarra.pertubis.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rafaelmarra.pertubis.model.disturb.Disturb
import com.example.rafaelmarra.pertubis.model.disturb.DisturbDatabase

class ListaViewModel(application: Application) : AndroidViewModel(application) {

    private val disturbDatabase: DisturbDatabase?

    var disturbList: LiveData<List<Disturb>>

    init {
        disturbDatabase = DisturbDatabase.getInstance(application)
        disturbList = disturbDatabase?.disturbDao()?.getAllLiveData()!!  //é sucesso
    }

    fun getDisturbData(){
        disturbList = disturbDatabase?.disturbDao()?.getAllLiveData()!!
    }

    fun deleteDisturb(disturb: Disturb) {
        val task = Runnable {
            disturbDatabase?.disturbDao()?.delete(disturb)
        }
        Thread(task).start()
    }

}