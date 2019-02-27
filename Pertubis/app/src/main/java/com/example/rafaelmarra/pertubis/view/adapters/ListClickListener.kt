package com.example.rafaelmarra.pertubis.view.adapters

import android.view.View

interface ListClickListener {

    fun onItemClick(position: Int)

    fun onItemLongPress(itemView: View): Boolean
}