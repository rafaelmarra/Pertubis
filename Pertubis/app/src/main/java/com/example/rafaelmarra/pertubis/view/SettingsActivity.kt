package com.example.rafaelmarra.pertubis.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.rafaelmarra.pertubis.R
import com.example.rafaelmarra.pertubis.databinding.ActivitySettingsBinding
import com.example.rafaelmarra.pertubis.viewmodel.RADIO_NEVER
import com.example.rafaelmarra.pertubis.viewmodel.SettingsViewModel


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBindings()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setBindings() {
        settingsViewModel = SettingsViewModel(application)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        binding.viewModel = settingsViewModel
        binding.executePendingBindings()
    }

    override fun onResume() {
        setOptions()
        super.onResume()
    }

    private fun setOptions() {
        binding.optionsRadioGroup.check(binding.viewModel?.getSelectedRadioOption() ?: RADIO_NEVER)
        binding.optionsRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            binding.viewModel?.saveSelectedId(checkedId)
        }
    }

    fun clickOk(view: View) {
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
