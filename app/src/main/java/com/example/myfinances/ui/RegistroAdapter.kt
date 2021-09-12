package com.example.myfinances.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
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
        for ((i, item) in listRegistro.withIndex()){
            for ((a,item2) in listRegistro.withIndex()){
                if((item.date == item2.date) and (i != a)){
                    item2.date = ""
                }
            }
        }
        holder.bind(listRegistro[position])
        holder.itemView.setOnClickListener { onItemClicked(listRegistro[position]) }
    }

    override fun getItemCount(): Int {
        return listRegistro.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun appendItem(newItems: MutableList<RegistroServer>, flag: Boolean) {
        listRegistro.addAll(newItems)
        if(flag) {
            notifyDataSetChanged()
            listRegistro.clear()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = CardViewRegistroItemBinding.bind(view)
        @SuppressLint("SetTextI18n")
        fun bind(registro: RegistroServer) {
            with(binding) {
                val dec = DecimalFormat("###,###,###,###,###,###,###,###.##")
                val number = dec.format(registro.amount)
                if(registro.date==""){
                    binding.card.updateLayoutParams { height = 42 }
                    fecha.visibility =  GONE
                }else  {
                    fecha.text = registro.date
                }
                value.text = "$ $number"
                tipo.text = registro.description
            }
        }
    }
}
