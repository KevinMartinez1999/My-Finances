package com.example.myfinances.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidplot.pie.Segment
import com.androidplot.pie.SegmentFormatter
import com.example.myfinances.R
import com.example.myfinances.data.EstadisticasItem
import com.example.myfinances.databinding.CardViewEstadisticasItemBinding
import com.example.myfinances.databinding.FragmentIngresosBinding
import java.text.DecimalFormat

class EstadisticasAdapter(private val onItemClicked: (EstadisticasItem) -> Unit) :
    RecyclerView.Adapter<EstadisticasAdapter.ViewHolder>() {

    private val listEstadisticas: MutableList<EstadisticasItem> = arrayListOf()
    private var flagColor: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_estadisticas_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listEstadisticas[position], flagColor)
        flagColor = !flagColor
        holder.itemView.setOnClickListener { onItemClicked(listEstadisticas[position]) }
    }

    override fun getItemCount(): Int {
        return listEstadisticas.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun appendItem(newItems: MutableList<EstadisticasItem>) {
        listEstadisticas.clear()
        listEstadisticas.addAll(newItems)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = CardViewEstadisticasItemBinding.bind(view)
        private val context: Context = binding.root.context

        fun bind(registro: EstadisticasItem, flagColor: Boolean) {
            val dec = DecimalFormat("###,###,###,###,###,###,###,###.##")
            val numero = dec.format(registro.amount)

            binding.tipo.text = registro.tipo
            binding.value.setTextColor(Color.BLACK)
            binding.value.text = context.getString(R.string.neutro, numero)

            if (flagColor) {
                binding.card.setCardBackgroundColor(Color.rgb(250, 250, 250))
            } else {
                binding.card.setCardBackgroundColor(Color.WHITE)
            }
        }
    }
}