package com.example.myfinances.fragments.sub_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfinances.data.server.RegistroServer
import com.example.myfinances.databinding.FragmentMensualBinding
import com.example.myfinances.ui.RegistroAdapterMonth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_mensual.*

class MensualFragment : Fragment() {

    private var _binding: FragmentMensualBinding? = null
    private val binding get() = _binding!!
    private lateinit var registroAdaptermensual: RegistroAdapterMonth
    private val db = Firebase.firestore
    private var listRegistros: MutableList<RegistroServer> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMensualBinding.inflate(inflater, container, false)

        registroAdaptermensual = RegistroAdapterMonth(onItemClicked = { onRegistroItemClicked(it) })
        binding.RecyclerViewMonth.apply {
            layoutManager = LinearLayoutManager(this@MensualFragment.context)
            adapter = registroAdaptermensual
            setHasFixedSize(false)
        }

        binding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val pos = binding.spinnerMonth.selectedItemPosition
                if (pos != 0) {
                    val mes = binding.spinnerMonth.selectedItem.toString()
                    loaddatosmensuales(pos)
                    Toast.makeText(
                        requireContext(),
                        "Datos del mes de $mes cargados con Éxito",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return binding.root
    }

    private fun loaddatosmensuales(mes: Int){
        val uid = Firebase.auth.currentUser?.uid.toString()
        var flag = false
        db.collection("registro").document(uid).collection("gastos").get().addOnSuccessListener { result->
            if (result.isEmpty) flag = true
        }
        loaddatos("ingresos", flag, mes)
        loaddatos("gastos", true, mes)
        listRegistros.clear()
    }

    private fun loaddatos(database : String, flag: Boolean, mes: Int) {
        val uid = Firebase.auth.currentUser?.uid.toString()
        db.collection("registro").document(uid).collection(database).get().addOnSuccessListener { result ->
            for (document in result) {
                listRegistros.add(document.toObject())
                listRegistros.sortByDescending(RegistroServer::date)
            }
            registroAdaptermensual.appendItem(listRegistros, flag, mes)
        }
    }

    private fun onRegistroItemClicked(registro: RegistroServer) {
        val fecha = registro.date
        Toast.makeText(requireContext(), "Transacción realizada el día $fecha", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.spinnerMonth.setSelection(0)
    }
}
