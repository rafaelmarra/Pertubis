package com.example.rafaelmarra.pertubis.viewmodel

import android.app.AlarmManager
import android.app.Application
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rafaelmarra.pertubis.R
import com.example.rafaelmarra.pertubis.model.cep.Cep
import com.example.rafaelmarra.pertubis.model.cep.CepDao
import com.example.rafaelmarra.pertubis.model.cep.ServiceListener
import com.example.rafaelmarra.pertubis.model.disturb.Disturb
import com.example.rafaelmarra.pertubis.model.disturb.DisturbDatabase
import com.example.rafaelmarra.pertubis.view.ListaActivity
import com.example.rafaelmarra.pertubis.viewmodel.business.CHANNEL_ID
import com.example.rafaelmarra.pertubis.viewmodel.business.NOTIFICATION
import com.example.rafaelmarra.pertubis.viewmodel.business.NOTIFICATION_ID
import com.example.rafaelmarra.pertubis.viewmodel.business.NotificationReceiver
import java.util.*

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

class DisturbViewModel(private val mApplication: Application) :
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

    val databaseReady: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val imageUrl: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    private var usesAddress = false
    private var isEdit = false
    private var disturbToEdit: Disturb? = null

    private val cepDao = CepDao()

    private val disturbDatabase: DisturbDatabase?

    init {
        disturbDatabase = DisturbDatabase.getInstance(mApplication)
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
            else -> "erro"
        }
        imageUrl.value = url
        txtQuem.set(parent?.getItemAtPosition(position).toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    fun setEditDisturb(disturb: Disturb) {
        isEdit = true
        disturbToEdit = disturb
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
        scheduleNotification(disturb.nome.toString(), disturb.como.toString(), null)
    }

    private fun saveNoAddressDisturb() {
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
        scheduleNotification(disturb.nome.toString(), disturb.como.toString(), null)
    }

    private fun editFullDisturb() {
        disturbToEdit?.nome = txtQuem.get()
        disturbToEdit?.como = txtComo.get()
        disturbToEdit?.hora = txtHora.get()
        disturbToEdit?.data = txtDia.get()
        disturbToEdit?.cep = txtCep.get()
        disturbToEdit?.endereco = txtEndereco.get()
        disturbToEdit?.bairro = txtBairro.get()
        disturbToEdit?.cidade = txtCidade.get()
        disturbToEdit?.estado = txtEstado.get()
        disturbToEdit?.numero = txtNumero.get()
        disturbToEdit?.complemento = txtComplemento.get()

        val disturbToUpdate = disturbToEdit as Disturb
        val task = Runnable {
            disturbDatabase?.disturbDao()?.update(disturbToUpdate)
        }
        Thread(task).start()
        databaseReady.value = true
    }

    private fun editNoAddressDisturb() {

        disturbToEdit?.nome = txtQuem.get()
        disturbToEdit?.como = txtComo.get()
        disturbToEdit?.hora = txtHora.get()
        disturbToEdit?.data = txtDia.get()
        disturbToEdit?.cep = null
        disturbToEdit?.endereco = null
        disturbToEdit?.bairro = null
        disturbToEdit?.cidade = null
        disturbToEdit?.estado = null
        disturbToEdit?.numero = null
        disturbToEdit?.complemento = null

        val disturbToUpdate = disturbToEdit as Disturb
        val task = Runnable {
            disturbDatabase?.disturbDao()?.update(disturbToUpdate)
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

    private fun scheduleNotification(disturbed: String, disturb: String, picture: Drawable?) {

        val notificationIntent = Intent(mApplication, NotificationReceiver::class.java).apply {
            putExtra(NOTIFICATION_ID, 1)
            putExtra(NOTIFICATION, getNotification(disturbed, disturb, picture))
        }

        val alarmIntent = PendingIntent.getBroadcast(
            mApplication,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val minuteToSet = txtHora.get()?.substring(3..4)?.toInt() ?: 0
        val hourToSet = txtHora.get()?.substring(0..1)?.toInt() ?: 0
        val dayToSet = txtDia.get()?.substring(0..1)?.toInt() ?: 0
        val monthToSet = txtDia.get()?.substring(3..4)?.toInt() ?: 0
        val yearToSet = txtDia.get()?.substring(6..9)?.toInt() ?: 0

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.MINUTE, minuteToSet)
            set(Calendar.HOUR_OF_DAY, hourToSet)
            set(Calendar.DAY_OF_MONTH, dayToSet)
            set(Calendar.MONTH, monthToSet - 1)
            set(Calendar.YEAR, yearToSet)
        }

        val alarmManager: AlarmManager = mApplication.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
    }

    private fun getNotification(disturbed: String, disturb: String, picture: Drawable?): Notification? {

        val intent = Intent(mApplication, ListaActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(mApplication, 0, intent, 0)

        val builder = NotificationCompat.Builder(mApplication, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_large)
            .setContentTitle("Hora de perturbar")
            .setContentText(
                "Não esqueça de perturbar ${if (disturbed == "Rosana") {
                    "a"
                } else {
                    "o"
                }} $disturbed!"
            )
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(disturb)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        return builder.build()
    }


}