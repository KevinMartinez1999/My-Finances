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
import com.example.myfinances.ui.RegistroAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_mensual.*
import java.text.SimpleDateFormat
import java.util.*




class MensualFragment : Fragment() {

    private var _binding: FragmentMensualBinding? = null
    private val binding get() = _binding!!
    private lateinit var registroAdaptermensual: RegistroAdapter
    private var listRegistros: MutableList<RegistroServer> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMensualBinding.inflate(inflater, container, false)

        val currentDate: String = SimpleDateFormat("MM", Locale.getDefault()).format(Date())
        loadFromServer(currentDate)
        binding.spinnerMonth.setSelection(currentDate.toInt())

        registroAdaptermensual = RegistroAdapter(onItemClicked = { onRegistroItemClicked(it) })
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
                cargar()

            }
        }

        binding.swiperefresh.setOnRefreshListener{
            cargar()
            binding.swiperefresh.isRefreshing =  false
        }

        return binding.root
    }

    private fun loadFromServer(mes: String) {
        val uid = Firebase.auth.currentUser?.uid.toString()
        val db = Firebase.firestore
        db.collection("registro")
            .document(uid)
            .collection("registropersonal")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val registro: RegistroServer = document.toObject()
                    if(registro.date?.contains("-$mes-") == true) {
                        listRegistros.add(registro)
                    }
                }
                registroAdaptermensual.appendItem(listRegistros)
                listRegistros.clear()
            }
    }

    private fun cargar() {
        val pos = binding.spinnerMonth.selectedItemPosition
        if (pos != 0) {
            val mes = if(pos<10){
                "0$pos"
            }else{
                "$pos"
            }
            loadFromServer(mes)
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

}
