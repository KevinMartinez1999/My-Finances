package com.example.myfinances.fragments.sub_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfinances.data.server.RegistroServer
import com.example.myfinances.databinding.FragmentDiarioBinding
import com.example.myfinances.ui.RegistroAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.card_view_registro_item.view.*

class DiarioFragment : Fragment() {

    private var _binding: FragmentDiarioBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private lateinit var registroAdapter: RegistroAdapter
    private var listRegistros: MutableList<RegistroServer> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiarioBinding.inflate(inflater, container, false)

        registroAdapter = RegistroAdapter(onItemClicked = { onRegistroItemClicked(it) })
        binding.RecyclerView.apply {
            layoutManager = LinearLayoutManager(this@DiarioFragment.context)
            adapter = registroAdapter
            setHasFixedSize(false)
        }
        loadFromServer()

        binding.swiperefresh.setOnRefreshListener {
            loadFromServer()
            binding.swiperefresh.isRefreshing = false
        }

        return binding.root
    }

    private fun loadFromServer() {
        loadDataBase("ingresos", false)
        loadDataBase("gastos", true)
        listRegistros.clear()
    }

    private fun loadDataBase(database: String, flag: Boolean) {
        val uid = Firebase.auth.currentUser?.uid.toString()
        db.collection("registro").document(uid).collection(database).get().addOnSuccessListener { result ->
            for (document in result) {
                listRegistros.add(document.toObject())
                listRegistros.sortByDescending(RegistroServer::date)
            }
            registroAdapter.appendItem(listRegistros, flag)
        }
    }

    private fun onRegistroItemClicked(registro: RegistroServer) {
        val msg = "Dinero " + registro.account
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
