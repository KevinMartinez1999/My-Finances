package com.example.myfinances.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfinances.R
import com.example.myfinances.data.server.RegistroServer
import com.example.myfinances.databinding.CardViewRegistroItemBinding
import java.text.DecimalFormat

class RegistroAdapter(private val onItemClicked: (RegistroServer) -> Unit) :
    RecyclerView.Adapter<RegistroAdapter.ViewHolder>() {

    private var listRegistro: MutableList<RegistroServer> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_registro_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listRegistro[position])
        holder.itemView.setOnClickListener { onItemClicked(listRegistro[position]) }
    }

    override fun getItemCount(): Int {
        return listRegistro.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun appendItem(newItems: MutableList<RegistroServer>) {
        listRegistro.clear()
        listRegistro.addAll(newItems)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = CardViewRegistroItemBinding.bind(view)
        @SuppressLint("SetTextI18n")
        fun bind(registro: RegistroServer) {
            with(binding) {
                val dec = DecimalFormat("###,###,###,###,###,###,###,###.##")
                val number = dec.format(registro.amount)
                value.text = "$ $number"
                fecha.text = registro.date
                tipo.text = registro.description
            }
        }
    }
}
