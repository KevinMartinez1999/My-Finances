package com.example.myfinances.fragments.sub_fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.myfinances.R
import com.example.myfinances.data.server.RegistroServer
import com.example.myfinances.databinding.FragmentDialogRegistroBinding
import com.example.myfinances.utils.EMPTY
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class DialogRegistroFragment : DialogFragment() {

    private var _binding: FragmentDialogRegistroBinding? = null
    private val binding get() = _binding!!
    private var cal = Calendar.getInstance()
    private var fecha: String = ""
    private var flag: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout to use as dialog or embedded fragment
        _binding = FragmentDialogRegistroBinding.inflate(inflater, container, false)

        binding.ingresospinner.visibility = View.VISIBLE
        binding.buttoningreso.isEnabled = false
        binding.valuetext.text = getString(R.string.valueIngresos)

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val format = SimpleDateFormat("dd-MM-yyyy", Locale.US)
            fecha = format.format(cal.time).toString()
            binding.inputdate.setText(fecha)
        }

        with(binding) {

            buttonCancel.setOnClickListener {
                onDestroyView()
            }

            inputdate.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            buttongasto.setOnClickListener {
                flag = true
                makeVisibleGasto()
            }

            buttoningreso.setOnClickListener {
                flag = false
                makeVisibleIngreso()
            }

            registerbutton.setOnClickListener {
                if (inputdate.text.isNotEmpty() and inputvalue.text.isNotEmpty()) {
                    registrarEnServer(flag)
                } else {
                    Toast.makeText(requireContext(), "Registro Inválido", Toast.LENGTH_LONG).show()
                }

            }
        }

        return binding.root
    }

    private fun registrarEnServer(type: Boolean) {
        with(binding) {
            val db = Firebase.firestore
            val category: String
            val id: String
            val uid = Firebase.auth.currentUser?.uid.toString()
            if (type) {
                val document = db.collection("registro").document(uid).collection("gastos").document()
                id = document.id
                category = gastospiner.selectedItem.toString()
            } else {
                val document = db.collection("registro").document(uid).collection("ingresos").document()
                id = document.id
                category = ingresospinner.selectedItem.toString()
            }

            val fecha = inputdate.text.toString()
            val cuenta = metodoPagoSpinner.selectedItem.toString()
            val monto = inputvalue.text.toString().toLong()

            val registro = RegistroServer(
                id = id,
                date = fecha,
                account = cuenta,
                description = category,
                amount = monto,
                type = type
            )
            if (type) {
                db.collection("registro").document(uid).collection("gastos").document(id).set(registro)
            } else {
                db.collection("registro").document(uid).collection("ingresos").document(id).set(registro)
            }
            clearViews()
            Toast.makeText(requireContext(), "Registro Exitoso", Toast.LENGTH_LONG).show()
        }
    }

    private fun clearViews() {
        with(binding) {
            inputdate.setText(EMPTY)
            inputvalue.setText(EMPTY)
        }
    }

    private fun makeVisibleIngreso() {
        with(binding) {
            gastospiner.visibility = View.GONE
            ingresospinner.visibility = View.VISIBLE
            categoryaccount.text = getString(R.string.account2)
            categorydescription.text = getString(R.string.category2)
            buttoningreso.isEnabled = false
            buttongasto.isEnabled = true
            valuetext.text = getString(R.string.valueIngresos)
        }
    }

    private fun makeVisibleGasto() {
        with(binding) {
            gastospiner.visibility = View.VISIBLE
            ingresospinner.visibility = View.GONE
            categoryaccount.text = getString(R.string.account)
            categorydescription.text = getString(R.string.category)
            buttoningreso.isEnabled = true
            buttongasto.isEnabled = false
            valuetext.text = getString(R.string.valueGastos)
        }
    }

    /** The system calls this only when creating the layout in a dialog. */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
