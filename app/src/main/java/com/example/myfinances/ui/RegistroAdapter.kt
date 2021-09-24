package com.example.myfinances.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
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
    private var flagColor: Boolean = true
    private var flagCard: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_registro_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listRegistro[position], flagColor, flagCard)
        flagColor = !flagColor
        holder.itemView.setOnClickListener { onItemClicked(listRegistro[position]) }
    }

    override fun getItemCount(): Int {
        return listRegistro.size
    }

    fun selectFragment(ocultar: Boolean){
        flagCard = ocultar
    }

    @SuppressLint("NotifyDataSetChanged")
    fun appendItem(newItems: MutableList<RegistroServer>) {
        listRegistro.clear()
        listRegistro.addAll(newItems)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = CardViewRegistroItemBinding.bind(view)
        private val context: Context = binding.root.context

        fun bind(registro: RegistroServer, flagColor: Boolean, flagCard: Boolean) {
            with(binding) {
                val dec = DecimalFormat("###,###,###,###,###,###,###,###.##")
                val number = dec.format(registro.amount)
                if (flagCard) descripcion.visibility = View.VISIBLE else descripcion.visibility = View.GONE
                if (registro.type == true) {
                    value.setTextColor(Color.BLUE)
                    value.text = context.getString(R.string.positive, number)
                } else {
                    value.text = context.getString(R.string.negative, number)
                    value.setTextColor(Color.RED)
                }
                tipo.text = registro.description
                descripcion.text = registro.date
                descripcion2.text = registro.account
                if (flagColor) {
                    card.setCardBackgroundColor(Color.rgb(250, 250, 250))
                }
                else {
                    card.setCardBackgroundColor(Color.WHITE)
                }
            }
        }
    }
}
