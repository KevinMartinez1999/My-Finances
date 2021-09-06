package com.example.myfinances.fragments.sub_fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfinances.data.server.RegistroServer
import com.example.myfinances.databinding.FragmentDiarioBinding
import com.example.myfinances.ui.RegistroAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class DiarioFragment : Fragment() {

    private var _binding: FragmentDiarioBinding? = null
    private val binding get() = _binding!!
    private lateinit var registroAdapter: RegistroAdapter
    private var listRegistros: MutableList<RegistroServer> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiarioBinding.inflate(inflater, container, false)

        registroAdapter = RegistroAdapter(onItemClicked = { onRegistroItemClicked() })
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
        val db = Firebase.firestore
        db.collection("registroingreso").get().addOnSuccessListener { result ->
            for (document in result) {
                listRegistros.add(document.toObject())
            }
            registroAdapter.appendItem(listRegistros)
        }
        db.collection("registrogasto").get().addOnSuccessListener { result ->
            for (document in result) {
                listRegistros.add(document.toObject())
            }
            registroAdapter.appendItem(listRegistros)
        }
    }

    private fun onRegistroItemClicked() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        Log.d("Pause","entró")
    }
}
