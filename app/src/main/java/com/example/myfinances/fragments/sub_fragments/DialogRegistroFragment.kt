package com.example.myfinances.fragments.sub_fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.myfinances.R
import com.example.myfinances.databinding.FragmentDialogRegistroBinding
import java.text.SimpleDateFormat
import java.util.*

class DialogRegistroFragment : DialogFragment() {


    private var _binding: FragmentDialogRegistroBinding? = null
    private val binding get() = _binding!!
    private var cal = Calendar.getInstance()
    private var fecha: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout to use as dialog or embedded fragment
        //return inflater.inflate(R.layout.fragment_registro_ingreso, container, false)
        _binding = FragmentDialogRegistroBinding.inflate(inflater, container, false)

        binding.categoryspiner.visibility = View.VISIBLE
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
                hacerVisibleGasto()
            }
            buttoningreso.setOnClickListener {
                hacerVisibleIngreso()
            }
        }

        return binding.root
    }

    private fun hacerVisibleIngreso() {
        with(binding) {
            categoryspiner.visibility = View.VISIBLE
            accountsspinner.visibility = View.GONE
            buttoningreso.isEnabled = false
            buttongasto.isEnabled = true
            valuetext.text = getString(R.string.valueIngresos)
        }
    }

    private fun hacerVisibleGasto() {
        with(binding) {
            categoryspiner.visibility = View.GONE
            accountsspinner.visibility = View.VISIBLE
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
