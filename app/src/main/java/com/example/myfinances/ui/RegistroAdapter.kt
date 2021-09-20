package com.example.myfinances.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfinances.R
import com.example.myfinances.data.server.RegistroServer
import com.example.myfinances.databinding.CardViewRegistroItemBinding
import com.example.myfinances.utils.EMPTY
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
        eliminarduplicados(listRegistro)
        holder.bind(listRegistro[position])
        holder.itemView.setOnClickListener { onItemClicked(listRegistro[position]) }
    }

    override fun getItemCount(): Int {
        return listRegistro.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun appendItem(newItems: MutableList<RegistroServer>, boolean: Boolean) {
        if (boolean) {
            listRegistro.clear()
        }
        listRegistro.addAll(newItems)
        notifyDataSetChanged()
    }

    private fun eliminarduplicados(list: MutableList<RegistroServer>) {
        for ((i, item) in list.withIndex()) {
            for ((a, item2) in list.withIndex()) {
                if ((item.date != "") and (item2.date != "")) {
                    if ((item.date == item2.date) and (i != a)) {
                        item2.date = ""
                    }
                }
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = CardViewRegistroItemBinding.bind(view)
        fun bind(registro: RegistroServer) {
            with(binding) {
                val dec = DecimalFormat("###,###,###,###,###,###,###,###.##")
                val number = dec.format(registro.amount)

                if (registro.date != EMPTY) { fecha.visibility = VISIBLE }
                else { fecha.visibility  = GONE }

                if (registro.type == true) {
                    value.setTextColor(Color.BLUE)
                    value.text = "+ $ $number"
                } else {
                    value.text = "- $ $number"
                    value.setTextColor(Color.RED)
                }
                tipo.text = registro.description
                fecha.text = registro.date
            }
        }
    }
}
