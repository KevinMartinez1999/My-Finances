package com.example.myfinances.fragments.sub_fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.DialogFragment
import com.example.myfinances.R
import com.example.myfinances.data.server.RegistroIngreso
import com.example.myfinances.databinding.FragmentDialogRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class DialogRegistroFragment : DialogFragment() {


    private var _binding: FragmentDialogRegistroBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private var cal = Calendar.getInstance()
    private var fecha: String = ""
    private var flag: Boolean = false;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout to use as dialog or embedded fragment
        //return inflater.inflate(R.layout.fragment_registro_ingreso, container, false)
        _binding = FragmentDialogRegistroBinding.inflate(inflater, container, false)

        binding.accountsspinner.visibility = View.VISIBLE
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
                flag=true;
                hacerVisibleGasto()
            }

            buttoningreso.setOnClickListener {
                flag=false
                hacerVisibleIngreso()
            }

            registerbutton.setOnClickListener {
                registrarenserver(flag)
            }
        }

        return binding.root
    }

    private fun registrarenserver(type:Boolean) {
        with(binding){
            val db = Firebase.firestore
            var category = ""
            var id = ""
            if(type){
                val document = db.collection("registrogasto").document()
                id = document.id
                category = categoryspiner.selectedItem.toString()
            }else{
                val document = db.collection("registroingreso").document()
                id = document.id
                category = accountsspinner.selectedItem.toString()
            }

            val fecha = inputdate.text.toString()
            val cuenta = metodoPagoSpinner.selectedItem.toString()
            val monto = inputvalue.text.toString().toLong()

            val registro = RegistroIngreso(id = id, date = fecha, account = cuenta, description = category, amount = monto)
            if(type){
                db.collection("registrogasto").document().set(registro)
            }else{
                db.collection("registroingreso").document().set(registro)
            }
            clearviews()
        }
    }

    private fun clearviews() {
            with(binding) {
                inputdate.setText("")
                inputvalue.setText("")
            }
    }

    private fun hacerVisibleIngreso() {
        with(binding) {
            categoryspiner.visibility = View.GONE
            accountsspinner.visibility = View.VISIBLE
            categoryaccount.text = getString(R.string.account2)
            categorytext.text = getString(R.string.category2)
            buttoningreso.isEnabled = false
            buttongasto.isEnabled = true
            valuetext.text = getString(R.string.valueIngresos)
        }
    }

    private fun hacerVisibleGasto() {
        with(binding) {
            categoryspiner.visibility = View.VISIBLE
            accountsspinner.visibility = View.GONE
            categoryaccount.text = getString(R.string.account)
            categorytext.text = getString(R.string.category)
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
