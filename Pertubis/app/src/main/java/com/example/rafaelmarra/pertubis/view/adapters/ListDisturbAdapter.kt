package com.example.rafaelmarra.pertubis.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.rafaelmarra.pertubis.R
import com.example.rafaelmarra.pertubis.model.disturb.Disturb
import kotlinx.android.synthetic.main.item_list_disturb.view.*

class ListDisturbAdapter(var disturbList: List<Disturb>, private val listener: ListClickListener):
    RecyclerView.Adapter<ListDisturbAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_disturb, parent, false)

        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(disturbList[position])
        setAnimation(holder.itemView)
    }

    private fun setAnimation(view: View) {
        val animation = AnimationUtils.loadAnimation(view.context, R.anim.slide_in_left)
        view.startAnimation(animation)
    }

    override fun getItemCount(): Int {
        return disturbList.size
    }

    fun update(list: List<Disturb>){
        this.disturbList = list
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, private val listener: ListClickListener) : RecyclerView.ViewHolder(itemView) {

        fun bind(disturb: Disturb) {

            itemView.txtListaNome.text = disturb.nome
            itemView.txtListaDesc.text = disturb.como
            itemView.txtListaData.text = disturb.data
            itemView.txtListaHora.text = disturb.hora

            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }

            itemView.setOnLongClickListener {
                listener.onItemLongPress(itemView)
            }
        }
    }
}