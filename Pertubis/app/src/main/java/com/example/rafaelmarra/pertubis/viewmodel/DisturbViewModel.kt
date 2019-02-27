package com.example.rafaelmarra.pertubis.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rafaelmarra.pertubis.model.cep.Cep
import com.example.rafaelmarra.pertubis.model.cep.CepDao
import com.example.rafaelmarra.pertubis.model.cep.ServiceListener
import com.example.rafaelmarra.pertubis.model.disturb.Disturb
import com.example.rafaelmarra.pertubis.model.disturb.DisturbDatabase

class DisturbViewModel(private val mApplication: Application) :
    AndroidViewModel(mApplication), ServiceListener {

    val txtQuem = ObservableField<String>()
    val txtComo = ObservableField<String>()
    val txtDia = ObservableField<String>("Selecionar data*")
    val txtHora = ObservableField<String>("Selecionar hora*")
    val txtCep = ObservableField<String>()
    val txtEndereco = ObservableField<String>("Endereço")
    val txtBairro = ObservableField<String>("Bairro")
    val txtCidade = ObservableField<String>("Cidade")
    val txtEstado = ObservableField<String>("Estado")
    val txtNumero = ObservableField<String>()
    val txtComplemento = ObservableField<String>()

    val databaseReady: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>()}

    private var usesAddress = false

    private val cepDao = CepDao()

    private val disturbDatabase: DisturbDatabase?

    init {
        disturbDatabase = DisturbDatabase.getInstance(mApplication)
    }


    //View

    fun pickDate(dia: Int, mes: Int, ano: Int) {
        val data = "$dia/$mes/$ano"
        txtDia.set(data)
    }

    fun pickTime(hora: Int, minutos: Int) {
        val horario = "$hora:$minutos"
        txtHora.set(horario)
    }

    //CEP

    fun checkCep() {
        usesAddress = false

        if (txtCep.get().isNullOrBlank()
            || txtCep.get()?.length != 9
        ) {
            Toast.makeText(mApplication, "CEP inválido", Toast.LENGTH_LONG).show()

        } else {
            cepDao.getCep(mApplication, txtCep.get().toString(), this)
        }
    }

    override fun onSucess(obtained: Any) {
        updateAddress(obtained as Cep)
    }

    override fun onError(throwable: Throwable) {
        Toast.makeText(mApplication, "CEP inválido", Toast.LENGTH_LONG).show()
    }

    private fun updateAddress(cep: Cep) {

        if (cep.erro) {
            Toast.makeText(mApplication, "CEP inválido", Toast.LENGTH_LONG).show()
            txtEndereco.set("Endereço")
            txtBairro.set("Bairro")
            txtCidade.set("Cidade")
            txtEstado.set("Estado")
            usesAddress = false

        } else {
            txtEndereco.set(cep.endereco)
            txtBairro.set(cep.bairro)
            txtCidade.set(cep.cidade)
            txtEstado.set(cep.estado)
            usesAddress = true
        }
    }


    //Database

    fun saveDisturb() {

        if (usesAddress
            && verifyFirstHalf()
            && verifySecondHalf()
        ) {
            val disturb = Disturb(
                null,
                txtQuem.get(),
                txtComo.get(),
                txtDia.get(),
                txtHora.get(),
                txtCep.get(),
                txtEndereco.get(),
                txtBairro.get(),
                txtCidade.get(),
                txtEstado.get(),
                txtNumero.get(),
                txtComplemento.get()
            )

            val task = Runnable {
                disturbDatabase?.disturbDao()?.insert(disturb)
            }
            Thread(task).start()
            databaseReady.value = true

        } else if (!usesAddress
            && verifyFirstHalf()
        ) {
            val disturb = Disturb(
                null,
                txtQuem.get(),
                txtComo.get(),
                txtDia.get(),
                txtHora.get(),
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
            val task = Runnable {
                disturbDatabase?.disturbDao()?.insert(disturb)
            }
            Thread(task).start()
            databaseReady.value = true

        } else {
            Toast.makeText(
                mApplication,
                "Não foi possível salvar. Verifique o preenchimento dos campos obrigatórios.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun verifyFirstHalf(): Boolean {

        if (txtQuem.get().isNullOrBlank()
            || txtComo.get().isNullOrBlank()
            || txtDia.get().isNullOrBlank()
            || txtHora.get().isNullOrBlank()
            || txtDia.get() == "Selecionar data*"
            || txtHora.get() == "Selecionar hora*"
        ) {
            return false
        }
        return true
    }

    private fun verifySecondHalf(): Boolean {

        if (txtCep.get().isNullOrBlank()) {
            return false
        }
        return true
    }


}