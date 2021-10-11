package com.example.myfinances.fragments.sub_fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myfinances.R
import com.example.myfinances.data.server.RegistroServer
import com.example.myfinances.databinding.FragmentHacerRegistroBinding
import com.example.myfinances.utils.EMPTY
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class HacerRegistroFragment : Fragment() {

    private var _binding: FragmentHacerRegistroBinding? = null
    private val binding get() = _binding!!
    private var cal = Calendar.getInstance()
    private var fecha: String = EMPTY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHacerRegistroBinding.inflate(inflater, container, false)

        val currentDate: String = SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.getDefault()
        ).format(Date())
        binding.inputdate.setText(currentDate)

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val format = SimpleDateFormat("dd-MM-yyyy", Locale.US)
            fecha = format.format(cal.time).toString()
            binding.inputdate.setText(fecha)
        }

        binding.tipoRegistro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                with(binding) {
                    if (position == 0) {
                        tipoDescripcionIngreso.visibility = VISIBLE
                        tipoDescripcionGasto.visibility = GONE
                    } else {
                        tipoDescripcionIngreso.visibility = GONE
                        tipoDescripcionGasto.visibility = VISIBLE
                    }
                }
            }
        }

        with(binding) {

            inputdate.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            registrar.setOnClickListener {
                if (inputdate.text.isNotEmpty() and inputvalue.text.isNotEmpty()) {
                    if (tipoRegistro.selectedItemPosition == 0) {
                        registrarEnServer(true)
                    } else {
                        registrarEnServer(false)
                    }
                } else {
                    toastMessage(getString(R.string.registro_invalido))
                }
            }
        }
        return binding.root
    }

    private fun registrarEnServer(type: Boolean) {
        with(binding) {
            val db = Firebase.firestore
            val id: String
            val uid = Firebase.auth.currentUser?.uid.toString()
            val document = db.collection("registro")
                .document(uid)
                .collection("registropersonal")
                .document()
            id = document.id

            val category: String = if (type) {
                tipoDescripcionIngreso.selectedItem.toString()
            } else {
                tipoDescripcionGasto.selectedItem.toString()
            }

            val fecha = inputdate.text.toString()
            val cuenta = tipoPago.selectedItem.toString()
            val monto = inputvalue.text.toString().toLong()

            val registro = RegistroServer(
                id = id,
                date = fecha,
                account = cuenta,
                description = category,
                amount = monto,
                type = type
            )

            db.collection("registro")
                .document(uid)
                .collection("registropersonal")
                .document(id)
                .set(registro)
            clearViews()
            toastMessage(getString(R.string.registro_exitoso))
            findNavController().navigate(R.id.action_HacerRegistroFragment_to_navigation_registro)
        }
    }

    private fun clearViews() {
        with(binding) {
            inputvalue.setText(EMPTY)
        }
    }

    private fun toastMessage(message: String) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
