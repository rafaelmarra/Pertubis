package com.example.rafaelmarra.pertubis.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.rafaelmarra.pertubis.R
import com.example.rafaelmarra.pertubis.databinding.ActivityDisturbBinding
import com.example.rafaelmarra.pertubis.extensions.applyTextMask
import com.example.rafaelmarra.pertubis.viewmodel.DisturbViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_disturb.*
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

class DisturbActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityDisturbBinding
    private lateinit var disturbViewModel: DisturbViewModel


    //Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        disturbViewModel = DisturbViewModel(application)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_disturb)
        binding.viewModel = disturbViewModel
        binding.executePendingBindings()

        binding.txtCepDist.applyTextMask("[00000]-[000]")

        binding.viewModel?.databaseReady?.observe(this, Observer {
            finish()
        })

        ArrayAdapter.createFromResource(
            this,
            R.array.names_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinnerNames.adapter = adapter
        }
        spinnerNames.background = getDrawable(R.drawable.spinner_background)
        spinnerNames.onItemSelectedListener = this
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
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

        Picasso.get()
            .load(url)
            .error(R.drawable.user)
            .into(imageView)
    }


    //View

    fun clickOpenCalendar(view: View) {
        val datePickerDialog = DatePickerDialog(this)
        datePickerDialog.show()
        datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            binding.viewModel?.pickDate(
                datePickerDialog.datePicker.dayOfMonth,
                datePickerDialog.datePicker.month + 1,
                datePickerDialog.datePicker.year
            )
            datePickerDialog.dismiss()
        }
    }

    fun clickOpenTime(view: View) {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val currentMinute = Calendar.getInstance().get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { timePicker, hourOfDay, minute ->
                binding.viewModel?.pickTime(
                    hourOfDay,
                    minute
                )
            },
            currentHour, currentMinute, true
        )

        timePickerDialog.show()
    }


    //CEP Api

    fun clickCep(view: View) {
        binding.viewModel?.checkCep()
    }


    //Database

    fun clickSave(view: View) {
        binding.viewModel?.saveDisturb()
    }
}

