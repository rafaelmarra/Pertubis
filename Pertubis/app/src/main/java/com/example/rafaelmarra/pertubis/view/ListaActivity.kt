package com.example.rafaelmarra.pertubis.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rafaelmarra.pertubis.R
import com.example.rafaelmarra.pertubis.databinding.ActivityListaBinding
import com.example.rafaelmarra.pertubis.model.disturb.Disturb
import com.example.rafaelmarra.pertubis.view.adapters.ListClickListener
import com.example.rafaelmarra.pertubis.view.adapters.ListDisturbAdapter
import com.example.rafaelmarra.pertubis.viewmodel.ListaViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_lista.*
import kotlinx.android.synthetic.main.content_lista.*

class ListaActivity : AppCompatActivity(), ListClickListener {

    private lateinit var binding: ActivityListaBinding
    private lateinit var listaViewModel: ListaViewModel

    private val adapter = ListDisturbAdapter(emptyList(), this)

    private var lastSelectedItem: View? = null
    private var lastSelectedDisturb: Disturb? = null

    private var menuIsShowing: Boolean? = null


    //Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBinding()
        setToolbar()
        setObservers()
        setRecyclerView()
    }

    private fun setBinding() {
        listaViewModel = ListaViewModel(application)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lista)
        binding.viewModel = listaViewModel
        binding.executePendingBindings()
    }

    private fun setToolbar() {
        setSupportActionBar(mToolbar)
        mToolbar.setTitleTextColor(getColor(R.color.design_default_color_background))
    }

    private fun setObservers() {
        listaViewModel.disturbList.observe(this, Observer {
            adapter.update(it)
        })
    }

    private fun setRecyclerView() {
        listDisturb.adapter = adapter
        listDisturb.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        binding.viewModel?.getDisturbData()
    }


    //View

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_vazia, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {

        when (menuIsShowing) {
            null -> {
                invalidateOptionsMenu()
                menuInflater.inflate(R.menu.menu_lista, menu)
                menuIsShowing = false

            }
            false -> {
                invalidateOptionsMenu()
                menuInflater.inflate(R.menu.menu_lista_vazia, menu)

            }
            true -> {
                invalidateOptionsMenu()
                menuInflater.inflate(R.menu.menu_lista, menu)
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }


    //Clicks

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.actionEdit -> {
                if (lastSelectedDisturb != null) {
                    val intent = Intent(this@ListaActivity, DisturbActivity::class.java)
                    val bundle = Bundle()

                    bundle.putLong("disturb", lastSelectedDisturb?.id as Long)
                    intent.putExtras(bundle)

                    startActivity(intent)
                    onItemClick()
                }
                true
            }

            R.id.actionDelete -> {
                if (lastSelectedDisturb != null) {
                    val disturbToDelete = lastSelectedDisturb
                    if (disturbToDelete != null) {
                        MaterialAlertDialogBuilder(this)
                            .setMessage("Deseja deletar esta perturbação?")
                            .setPositiveButton(
                                "Sim"
                            ) { _, _ ->
                                binding.viewModel?.deleteDisturb(disturbToDelete)
                                onItemClick()
                            }
                            .setNegativeButton("Não", null)
                            .show()
                    }

                }
                true
            }

            else -> false
        }
    }

    override fun onItemClick() {
        lastSelectedItem?.setBackgroundColor(getColor(R.color.design_default_color_background))

        lastSelectedItem = null
        lastSelectedDisturb = null

        if (menuIsShowing == true) {
            onPrepareOptionsMenu(mToolbar.menu)
            menuIsShowing = false
        }
    }

    override fun onItemLongPress(itemView: View, disturb: Disturb): Boolean {
        lastSelectedItem?.setBackgroundColor(getColor(R.color.design_default_color_background))

        itemView.setBackgroundColor(getColor(R.color.divider))

        lastSelectedItem = itemView
        lastSelectedDisturb = disturb

        if (menuIsShowing == false) {
            onPrepareOptionsMenu(mToolbar.menu)
            menuIsShowing = true
        }

        return true
    }

    fun fabClick(view: View) {
        onItemClick()
        startActivity(Intent(this@ListaActivity, DisturbActivity::class.java))
    }
}
