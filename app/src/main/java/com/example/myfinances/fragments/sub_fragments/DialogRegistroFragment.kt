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


    private var _binding: FragmentDialogRegistroBinding?= null
    private val binding get() = _binding!!
    private var cal = Calendar.getInstance()
    private var fecha:String = ""

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            // Inflate the layout to use as dialog or embedded fragment
            //return inflater.inflate(R.layout.fragment_registro_ingreso, container, false)
        _binding = FragmentDialogRegistroBinding.inflate(inflater, container, false)

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val format = SimpleDateFormat("dd-MM-yyyy", Locale.US)
            fecha = format.format(cal.time).toString()
            binding.inputdate.setText(fecha)
        }

        with(binding){
            inputdate.setOnClickListener {
                DatePickerDialog(requireContext(),
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            buttongasto.setOnClickListener {
                hacervisiblegasto(View.VISIBLE)

            }
            buttoningreso.setOnClickListener {
                hacervisibleingreso(View.VISIBLE)
            }


        }



        return binding.root
        }

    private fun hacervisibleingreso(vista: Int) {
        with(binding) {
            inputdate.visibility = vista
            categoryaccount.text = getString(R.string.account2)
            categoryaccount.visibility = vista
            categorytext.text = getString(R.string.category2)
            categorytext.visibility = vista
            categoryspiner.visibility = View.INVISIBLE
            accountsspinner.visibility = vista
            accountspinner.visibility = vista
            valuetext.visibility = vista
            inputvalue.visibility = vista
            registerbutton.visibility = vista

        }
    }

    private fun hacervisiblegasto(vista: Int) {
        with(binding) {
            inputdate.visibility = vista
            categoryaccount.text = getString(R.string.account)
            categoryaccount.visibility = vista
            categorytext.text = getString(R.string.category)
            categorytext.visibility = vista
            categoryspiner.visibility = vista
            accountsspinner.visibility = View.INVISIBLE
            accountspinner.visibility = vista
            valuetext.visibility = vista
            inputvalue.visibility = vista
            registerbutton.visibility = vista
        }
    }

    /** The system calls this only when creating the layout in a dialog. */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

}