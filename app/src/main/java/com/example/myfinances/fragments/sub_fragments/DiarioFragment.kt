package com.example.myfinances.fragments.sub_fragments

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfinances.R
import com.example.myfinances.data.server.RegistroServer
import com.example.myfinances.databinding.FragmentDiarioBinding
import com.example.myfinances.fragments.RegistroFragmentDirections
import com.example.myfinances.ui.RegistroAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class DiarioFragment : Fragment() {

    private var _binding: FragmentDiarioBinding? = null
    private val binding get() = _binding!!
    private lateinit var registroAdapter: RegistroAdapter
    private var cal = Calendar.getInstance()
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

        val currentDate: String = SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.getDefault()
        ).format(Date())
        binding.textDate.setText(currentDate)

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val format = SimpleDateFormat("dd-MM-yyyy", Locale.US)
            val fecha = format.format(cal.time).toString()
            binding.textDate.setText(fecha)
            loadFromServer(binding.textDate.text.toString())
        }

        with(binding) {

            textDate.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            swiperefresh.setOnRefreshListener {
                registroAdapter.flagColor = true
                loadFromServer(binding.textDate.text.toString())
                binding.swiperefresh.isRefreshing = false
            }
        }

        return binding.root
    }

    private fun loadFromServer(date: String) {
        registroAdapter.selectFragment(false)
        val uid = Firebase.auth.currentUser?.uid.toString()
        val db = Firebase.firestore
        db.collection("registro")
            .document(uid)
            .collection("registropersonal")
            .get()
            .addOnSuccessListener { result ->
                var balance: Long = 0
                for (document in result) {
                    val registro: RegistroServer = document.toObject()
                    if (registro.date == date) {
                        listRegistros.add(registro)
                        if (registro.type == true) {
                            balance += registro.amount!!
                        } else {
                            balance -= registro.amount!!
                        }
                    }
                }
                setBalance(balance)
                registroAdapter.appendItem(listRegistros)
                listRegistros.clear()
            }
    }

    private fun setBalance(balance: Long) {
        val context: Context = binding.root.context
        val dec = DecimalFormat("###,###,###,###,###,###,###,###.##")
        if (balance >= 0) {
            val num = dec.format(balance)
            binding.valueBalance.setTextColor(Color.BLUE)
            binding.valueBalance.text =
                context.getString(R.string.positive, num)
        } else {
            val num = dec.format(kotlin.math.abs(balance))
            binding.valueBalance.setTextColor(Color.RED)
            binding.valueBalance.text =
                context.getString(R.string.negative, num)
        }
    }

    private fun onRegistroItemClicked(registro: RegistroServer) {
        findNavController().navigate(
            RegistroFragmentDirections.actionNavigationRegistroToNavigationEditRegistro(
                register = registro
            )
        )
    }

    override fun onResume() {
        super.onResume()
        val fecha = binding.textDate.text.toString()
        if (fecha.isNotEmpty()) {
            loadFromServer(fecha)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
