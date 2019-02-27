package com.example.rafaelmarra.pertubis.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rafaelmarra.pertubis.R
import com.example.rafaelmarra.pertubis.databinding.ActivityListaBinding
import com.example.rafaelmarra.pertubis.view.adapters.ListClickListener
import com.example.rafaelmarra.pertubis.view.adapters.ListDisturbAdapter
import com.example.rafaelmarra.pertubis.viewmodel.ListaViewModel
import kotlinx.android.synthetic.main.activity_lista.*
import kotlinx.android.synthetic.main.content_lista.*

class ListaActivity : AppCompatActivity(), ListClickListener {

    lateinit var binding: ActivityListaBinding
    private lateinit var listaViewModel: ListaViewModel

    private val adapter = ListDisturbAdapter(emptyList(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listaViewModel = ListaViewModel(application)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_lista)
        binding.viewModel = listaViewModel
        binding.executePendingBindings()

        setSupportActionBar(mtoolbar)

        listaViewModel.disturbList.observe(this, Observer {
            adapter.update(it)
        })

        listDisturb.adapter = adapter
        listDisturb.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        binding.viewModel?.getDisturbData()
    }

    fun fabClick(view: View) {
        startActivity(Intent(this@ListaActivity, DisturbActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_lista, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onItemClick(position: Int) {
        Toast.makeText(this, "cliquei!", Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongPress(itemView: View): Boolean {
        Toast.makeText(this, "afundei!", Toast.LENGTH_SHORT).show()

        return true
    }
}
