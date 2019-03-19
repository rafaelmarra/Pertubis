package com.example.rafaelmarra.pertubis.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.rafaelmarra.pertubis.R
import com.example.rafaelmarra.pertubis.databinding.ActivityDisturbBinding
import com.example.rafaelmarra.pertubis.extensions.applyTextMask
import com.example.rafaelmarra.pertubis.extensions.hideKeyboard
import com.example.rafaelmarra.pertubis.viewmodel.DisturbViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_disturb.*
import java.util.*

class DisturbActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDisturbBinding
    private lateinit var disturbViewModel: DisturbViewModel


    //Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBinding()
        setTextMasks()
        setObservers()
        setNameSpinner()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.extras?.getLong("disturb") != null) {
            setEditActivity()
            supportActionBar?.title = "Editar"

        } else {
            supportActionBar?.title = "Criar"
        }
    }

    private fun setBinding() {
        disturbViewModel = DisturbViewModel(application, intent.extras?.getLong("disturb"))
        binding = DataBindingUtil.setContentView(this, R.layout.activity_disturb)
        binding.viewModel = disturbViewModel
        binding.executePendingBindings()
    }

    private fun setTextMasks() {
        binding.txtCepDist.applyTextMask("[00000]-[000]")
    }

    private fun setObservers() {
        binding.viewModel?.databaseReady?.observe(this, Observer {
            finish()
        })

        binding.viewModel?.imageUrl?.observe(this, Observer {
            Picasso.get()
                .load(it)
                .error(R.drawable.user)
                .into(imgNome)
        })

        binding.viewModel?.disturbToEdit?.observe(this, Observer {
            binding.viewModel?.setEditDisturb(it)
            setEditActivity()
        })
    }

    private fun setNameSpinner() {

        ArrayAdapter.createFromResource(
            this,
            R.array.names_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerNames.adapter = adapter
        }

        spinnerNames.background = getDrawable(R.drawable.spinner_background)
        spinnerNames.onItemSelectedListener = binding.viewModel
    }

    private fun setEditActivity() {
        spinnerNames.setSelection(binding.viewModel?.nameToSet() ?: 0)
    }


    //Clicks

    fun clickOpenCalendar(view: View) {

        hideKeyboard(this)

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

        hideKeyboard(this)

        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val currentMinute = Calendar.getInstance().get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                binding.viewModel?.pickTime(hourOfDay, minute)
            },
            currentHour, currentMinute, true
        )

        timePickerDialog.show()
    }

    fun clickCep(view: View) {
        hideKeyboard(this)
        binding.viewModel?.checkCep()
    }

    fun clickSave(view: View) {
        binding.viewModel?.saveDisturb()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}

