package com.example.rafaelmarra.pertubis.viewmodel

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

@BindingAdapter("setAdapter")
fun bindListAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    recyclerView.apply {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(recyclerView.context)
        this.adapter = adapter
    }
}