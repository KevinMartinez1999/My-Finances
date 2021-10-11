package com.example.myfinances.fragments.sub_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfinances.R
import com.example.myfinances.data.EstadisticasItem
import com.example.myfinances.data.server.RegistroServer
import com.example.myfinances.databinding.FragmentIngresosBinding
import com.example.myfinances.ui.EstadisticasAdapter
import com.example.myfinances.utils.colors
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_ingresos.*
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import java.text.SimpleDateFormat
import java.util.*

class IngresosFragment : Fragment() {

    private var _binding: FragmentIngresosBinding? = null
    private val binding get() = _binding!!
    private lateinit var estadisticasAdapter: EstadisticasAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIngresosBinding.inflate(inflater, container, false)
        estadisticasAdapter = EstadisticasAdapter(onItemClicked = { onRegistroItemClicked() })
        binding.RecyclerView.apply {
            layoutManager = LinearLayoutManager(this@IngresosFragment.context)
            adapter = estadisticasAdapter
            setHasFixedSize(false)
        }

        val mes: String = SimpleDateFormat(
            "MM",
            Locale.getDefault()
        ).format(Date())

        val year: String = SimpleDateFormat(
            "yyyy",
            Locale.getDefault()
        ).format(Date())

        binding.mes.setSelection(mes.toInt())
        binding.ano.setSelection(year.toInt() - 2000)

        loadFromServer(mes, year)

        binding.mes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val pos = binding.mes.selectedItemPosition
                val y = binding.ano.selectedItem.toString()

                val m = if (pos < 10) {
                    "0$pos"
                } else {
                    "$pos"
                }

                estadisticasAdapter.flagColor = true
                loadFromServer(m, y)
            }
        }

        binding.ano.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val pos = binding.mes.selectedItemPosition
                val y = binding.ano.selectedItem.toString()

                val m = if (pos < 10) {
                    "0$pos"
                } else {
                    "$pos"
                }

                estadisticasAdapter.flagColor = true
                loadFromServer(m, y)
            }
        }

        return binding.root
    }

    private fun loadFromServer(mes: String, year: String) {
        val uid = Firebase.auth.currentUser?.uid.toString()
        val db = Firebase.firestore
        db.collection("registro")
            .document(uid)
            .collection("registropersonal")
            .get()
            .addOnSuccessListener { result ->
                val listEstadisticas: MutableList<EstadisticasItem> = arrayListOf()
                val lista = resources.getStringArray(R.array.ingresoslist)
                for (i in lista) {
                    listEstadisticas.add(EstadisticasItem(i, 0, true))
                }
                for (document in result) {
                    val registro: RegistroServer = document.toObject()
                    if (registro.date?.contains("-$mes-$year") == true && registro.type == true
                    ) {
                        for (item in listEstadisticas) {
                            if (item.tipo == registro.description) {
                                item.amount = item.amount?.plus(registro.amount!!)
                            }
                        }
                    }
                }
                val aux: MutableList<EstadisticasItem> = arrayListOf()
                for (i in listEstadisticas) {
                    if (i.amount == 0L) {
                        aux.add(i)
                    }
                }
                listEstadisticas.removeAll(aux)
                estadisticasAdapter.appendItem(listEstadisticas)
                drawPiechart(listEstadisticas)
            }
    }

    private fun drawPiechart(items: MutableList<EstadisticasItem>) {
        val pieData: MutableList<SliceValue> = arrayListOf()
        var color = colors.random()
        val coloresUsados: MutableList<Int> = arrayListOf()
        coloresUsados.add(color)
        for (item in items) {
            pieData.add(SliceValue(item.amount!!.toFloat(), color).setLabel(item.tipo))
            do {
                color = colors.random()
            } while (coloresUsados.contains(color))
            coloresUsados.add(color)
        }
        val pieChartData = PieChartData(pieData)
        pieChartData.setHasLabels(true)
        binding.chart.pieChartData = pieChartData
    }

    private fun onRegistroItemClicked() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
