package com.example.rafaelmarra.pertubis.view.adapters

import android.view.View
import com.example.rafaelmarra.pertubis.model.disturb.Disturb

interface ListClickListener {

    fun onItemClick()

    fun onItemLongPress(itemView: View, disturb: Disturb): Boolean
}