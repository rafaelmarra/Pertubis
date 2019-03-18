package com.example.rafaelmarra.pertubis.viewmodel

import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rafaelmarra.pertubis.extensions.cancelAlarm
import com.example.rafaelmarra.pertubis.extensions.createAlarmWithCode
import com.example.rafaelmarra.pertubis.extensions.formatTime
import com.example.rafaelmarra.pertubis.model.cep.Cep
import com.example.rafaelmarra.pertubis.model.cep.CepDao
import com.example.rafaelmarra.pertubis.model.cep.ServiceListener
import com.example.rafaelmarra.pertubis.model.disturb.Disturb
import com.example.rafaelmarra.pertubis.model.disturb.DisturbDatabase

const val ROBERTO_CARLOS =
    "https://scontent.frao1-1.fna.fbcdn.net/v/t1.0-9/51149945_124304005297850_6080370671072837632_n.jpg?_nc_cat=108&_nc_pt=1&_nc_ht=scontent.frao1-1.fna&oh=3ea1371aeb63dd402faf775074f726ff&oe=5CEA7402"
const val MARRA =
    "https://scontent.frao1-1.fna.fbcdn.net/v/t1.0-9/50686027_108966820222773_7126009948529491968_n.jpg?_nc_cat=107&_nc_pt=1&_nc_ht=scontent.frao1-1.fna&oh=53fa9f7456c9851e232aef1da2323dae&oe=5D2285F6"
const val ROSANA =
    "https://scontent.frao1-2.fna.fbcdn.net/v/t1.0-9/51152747_108358606961024_1219213212424077312_n.jpg?_nc_cat=103&_nc_pt=1&_nc_ht=scontent.frao1-2.fna&oh=d28f16a20db4dc5bca8af43774137d6a&oe=5D210BAB"
const val CARECA =
    "https://scontent.frao1-2.fna.fbcdn.net/v/t1.0-9/51033605_373994360093165_3580441865029156864_n.jpg?_nc_cat=101&_nc_pt=1&_nc_ht=scontent.frao1-2.fna&oh=c933e1e63966a5c5b8241ec99c20e88c&oe=5CE662C0"
const val PEPE =
    "https://scontent.frao1-2.fna.fbcdn.net/v/t1.0-9/49376865_101377337617421_1283925248449708032_n.jpg?_nc_cat=101&_nc_pt=1&_nc_ht=scontent.frao1-2.fna&oh=f5e479c5a99b6d1d40187a7b93431dbe&oe=5D16DD6D"
const val ALE =
    "https://scontent-gru2-1.xx.fbcdn.net/v/t1.0-9/29261678_140904916738779_5851668644722900992_n.jpg?_nc_cat=105&_nc_pt=1&_nc_ht=scontent-gru2-1.xx&oh=c0d79f01ce464ed912ea5b6de0ef7227&oe=5D0237E3"

class DisturbViewModel(private val mApplication: Application, idToEdit: Long?) :
    AndroidViewModel(mApplication), ServiceListener, AdapterView.OnItemSelectedListener {

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
    val txtButton = ObservableField<String>("Salvar")

    val databaseReady: MutableLiveData<Boolean> = MutableLiveData()
    val imageUrl: MutableLiveData<String> = MutableLiveData()

    private var usesAddress = false
    private var isEdit = false

    private val cepDao = CepDao()

    private val disturbDatabase: DisturbDatabase? = DisturbDatabase.getInstance(mApplication)
    val disturbToEdit: LiveData<Disturb>? = if (idToEdit != null) {
        disturbDatabase?.disturbDao()?.getFromId(idToEdit)
    } else {
        null
    }


    //View

    fun pickDate(dia: Int, mes: Int, ano: Int) {

        val diaString = String.format("%02d", dia)
        val mesString = String.format("%02d", mes)
        val anoString = String.format("%04d", ano)
        val data = "$diaString/$mesString/$anoString"
        txtDia.set(data)
    }

    fun pickTime(hora: Int, minutos: Int) {

        val horaString = String.format("%02d", hora)
        val minString = String.format("%02d", minutos)
        val horario = "$horaString:$minString"
        txtHora.set(horario)
    }

    fun nameToSet(): Int {
        return when (txtQuem.get()) {
            "Roberto Carlos" -> 1
            "Marra" -> 2
            "Rosana" -> 3
            "Careca" -> 4
            "Pepe" -> 5
            "Ale" -> 6
            else -> 0
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val url = when (position) {
            1 -> ROBERTO_CARLOS
            2 -> MARRA
            3 -> ROSANA
            4 -> CARECA
            5 -> PEPE
            6 -> ALE
            else -> "erro"
        }
        imageUrl.value = url
        txtQuem.set(parent?.getItemAtPosition(position).toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    fun setEditDisturb(disturb: Disturb) {
        isEdit = true
        txtButton.set("Editar")

        txtQuem.set(disturb.nome)
        txtComo.set(disturb.como)
        txtDia.set(disturb.data)
        txtHora.set(disturb.hora)
        txtCep.set(disturb.cep)
        txtEndereco.set(
            if (disturb.endereco == null) {
                "Sem endereço"
            } else {
                disturb.endereco
            }
        )
        txtBairro.set(disturb.bairro)
        txtCidade.set(disturb.cidade)
        txtEstado.set(disturb.estado)
        txtNumero.set(disturb.numero)
        txtComplemento.set(disturb.complemento)

        if (disturb.endereco != null) {
            usesAddress = true
        }
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
            && !isEdit
            && verifyFirstHalf()
            && verifySecondHalf()
        ) {
            saveFullDisturb()

        } else if (!usesAddress
            && !isEdit
            && verifyFirstHalf()
        ) {
            saveNoAddressDisturb()

        } else if (isEdit
            && usesAddress
            && verifyFirstHalf()
            && verifySecondHalf()
        ) {
            editFullDisturb()

        } else if (isEdit
            && !usesAddress
            && verifyFirstHalf()
        ) {
            editNoAddressDisturb()

        } else {
            Toast.makeText(
                mApplication,
                "Não foi possível salvar. Verifique o preenchimento dos campos obrigatórios.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun saveFullDisturb() {

        val intentCode = scheduleNotification()

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
            txtComplemento.get(),
            intentCode
        )

        val task = Runnable {
            disturbDatabase?.disturbDao()?.insert(disturb)
        }
        Thread(task).start()

        databaseReady.value = true
    }

    private fun saveNoAddressDisturb() {

        val intentCode = scheduleNotification()

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
            null,
            intentCode
        )

        val task = Runnable {
            disturbDatabase?.disturbDao()?.insert(disturb)
        }
        Thread(task).start()

        databaseReady.value = true
    }

    private fun editFullDisturb() {

        val disturb = disturbToEdit?.value

        val intentCode = disturb?.intentCode
        if (intentCode != null) {
            cancelAlarm(mApplication, intentCode)
        }
        val newIntentCode = scheduleNotification()

        disturb?.nome = txtQuem.get()
        disturb?.como = txtComo.get()
        disturb?.hora = txtHora.get()
        disturb?.data = txtDia.get()
        disturb?.cep = txtCep.get()
        disturb?.endereco = txtEndereco.get()
        disturb?.bairro = txtBairro.get()
        disturb?.cidade = txtCidade.get()
        disturb?.estado = txtEstado.get()
        disturb?.numero = txtNumero.get()
        disturb?.complemento = txtComplemento.get()
        disturb?.intentCode = newIntentCode

        val task = Runnable {
            if (disturb != null) {
                disturbDatabase?.disturbDao()?.update(disturb)
            }
        }
        Thread(task).start()
        databaseReady.value = true
    }

    private fun editNoAddressDisturb() {

        val disturb = disturbToEdit?.value

        val intentCode = disturb?.intentCode
        if (intentCode != null) {
            cancelAlarm(mApplication, intentCode)
        }
        val newIntentCode = scheduleNotification()

        disturb?.nome = txtQuem.get()
        disturb?.como = txtComo.get()
        disturb?.hora = txtHora.get()
        disturb?.data = txtDia.get()
        disturb?.cep = null
        disturb?.endereco = null
        disturb?.bairro = null
        disturb?.cidade = null
        disturb?.estado = null
        disturb?.numero = null
        disturb?.complemento = null
        disturb?.intentCode = newIntentCode

        val task = Runnable {
            if (disturb != null) {
                disturbDatabase?.disturbDao()?.update(disturb)
            }
        }
        Thread(task).start()
        databaseReady.value = true
    }

    private fun verifyFirstHalf(): Boolean {

        if (txtQuem.get().isNullOrBlank()
            || txtQuem.get() == "Selecione"
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

    private fun scheduleNotification(): Int {
        val timeToSet = formatTime(txtHora.get().toString(), txtDia.get().toString())

        return createAlarmWithCode(mApplication,
            txtQuem.get().toString(),
            txtComo.get().toString(),
            timeToSet)
    }
}