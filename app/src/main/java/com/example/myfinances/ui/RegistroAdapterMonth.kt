package com.example.myfinances.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfinances.R
import com.example.myfinances.data.server.RegistroServer
import com.example.myfinances.databinding.CardViewMonthBinding
import java.text.DecimalFormat

class RegistroAdapterMonth(private val onItemClicked: (RegistroServer) -> Unit) :
    RecyclerView.Adapter<RegistroAdapterMonth.ViewHolder>() {

    private var listRegistro: MutableList<RegistroServer> = mutableListOf()
    private lateinit var mes: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_month, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listRegistro[position], mes)
        holder.itemView.setOnClickListener { onItemClicked(listRegistro[position]) }
    }

    override fun getItemCount(): Int {
        return listRegistro.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun appendItem(newItems: MutableList<RegistroServer>, flag: Boolean, month: Int) {
        listRegistro.addAll(newItems)
        if(flag) {
            mes = if(month < 10) {
                "0$month"
            } else{
                "$month"
            }
            notifyDataSetChanged()
            listRegistro.clear()
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = CardViewMonthBinding.bind(view)
        @SuppressLint("SetTextI18n", "ResourceAsColor")
        fun bind(registro: RegistroServer, mes: String) {
            with(binding) {
                val dec = DecimalFormat("###,###,###,###,###,###,###,###.##")
                val number = dec.format(registro.amount)
                var sign: String
                valueMonth.text = ""
                description.text = ""
                if (registro.type == true){
                    sign = "-"
                }else{
                    sign = "+"
                    valueMonth.setTextColor(R.color.md_blue_900)
                }
                if(registro.date?.contains("-$mes-") == true) {
                    valueMonth.text = "$sign $ $number"
                    description.text = registro.description
                }
            }
        }
    }
}
