package com.example.myfinances.fragments.sub_fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.myfinances.R
import com.example.myfinances.databinding.FragmentEditarRegistroBinding
import com.example.myfinances.utils.EMPTY
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class EditarRegistroFragment : Fragment() {

    private var _binding: FragmentEditarRegistroBinding? = null
    private val binding get() = _binding!!
    private val args: EditarRegistroFragmentArgs by navArgs()
    private var cal = Calendar.getInstance()
    private var fecha: String = EMPTY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditarRegistroBinding.inflate(inflater, container, false)

        val registro = args.register

        with(binding) {
            inputdate.setText(registro?.date.toString()) //Fecha
            inputvalue.setText(registro?.amount.toString()) //monto
            if (registro?.type == true) { //Tipo de registro
                tipoRegistro.setSelection(0)
                tipoDescripcionIngreso.visibility = View.VISIBLE
                tipoDescripcionGasto.visibility = View.GONE
            } else {
                tipoRegistro.setSelection(1)
                tipoDescripcionIngreso.visibility = View.GONE
                tipoDescripcionGasto.visibility = View.VISIBLE
            }
            when (registro?.account) { //Tipo de cuenta
                "Efectivo" -> {
                    tipoPago.setSelection(0)
                }
                "Cuenta Bancaria" -> {
                    tipoPago.setSelection(1)
                }
                "Tarjeta de Crédito" -> {
                    tipoPago.setSelection(2)
                }
            }

            //Descripcion
            var esIngreso = true
            var position = 0
            for ((i, item) in resources.getStringArray(R.array.ingresoslist).withIndex()) {
                if (registro?.description.toString() == item.toString()) {
                    esIngreso = true
                    position = i
                }
            }
            for ((i, item) in resources.getStringArray(R.array.categorylist).withIndex()) {
                if (registro?.description.toString() == item.toString()) {
                    esIngreso = false
                    position = i
                }
            }
            if (esIngreso) {
                tipoDescripcionIngreso.setSelection(position)
            } else {
                tipoDescripcionGasto.setSelection(position)
            }

            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val format = SimpleDateFormat("dd-MM-yyyy", Locale.US)
                fecha = format.format(cal.time).toString()
                binding.inputdate.setText(fecha)
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

            binding.tipoRegistro.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
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
                                tipoDescripcionIngreso.visibility = View.VISIBLE
                                tipoDescripcionGasto.visibility = View.GONE
                            } else {
                                tipoDescripcionIngreso.visibility = View.GONE
                                tipoDescripcionGasto.visibility = View.VISIBLE
                            }
                        }
                    }
                }


            val uid = Firebase.auth.currentUser?.uid.toString()
            val db = Firebase.firestore
            val id = registro?.id.toString()
            val context: Context = binding.root.context

            borrar.setOnClickListener {

                MaterialAlertDialogBuilder(context)
                    .setTitle(getString(R.string.confirmacion))
                    .setMessage(getString(R.string.confirmar_borrar))
                    .setNeutralButton(getString(R.string.cancelar)) { _, _ ->
                        // Respond to neutral button press
                    }
                    .setPositiveButton(getString(R.string.aceptar)) { _, _ ->
                        db.collection("registro")
                            .document(uid)
                            .collection("registropersonal")
                            .document(id).delete()
                        toastMessage(getString(R.string.registro_eliminado))
                        findNavController().navigate(R.id.action_EditarRegistroFragment_to_navigation_registro)
                    }
                    .show()
            }

            editar.setOnClickListener {

                MaterialAlertDialogBuilder(context)
                    .setTitle(getString(R.string.confirmacion))
                    .setMessage(getString(R.string.confirmar_editar))
                    .setNeutralButton(getString(R.string.cancelar)) { _, _ ->
                        // Respond to neutral button press
                    }
                    .setPositiveButton(getString(R.string.aceptar)) { _, _ ->

                        val documentUpdate = HashMap<String, Any>()
                        if (inputdate.text.isNotEmpty() and inputvalue.text.isNotEmpty()) {
                            documentUpdate["date"] = inputdate.text.toString()
                            documentUpdate["account"] = tipoPago.selectedItem.toString()
                            if (tipoRegistro.selectedItemPosition == 0) {
                                documentUpdate["description"] =
                                    tipoDescripcionIngreso.selectedItem.toString()
                                documentUpdate["type"] = true
                            } else {
                                documentUpdate["description"] =
                                    tipoDescripcionGasto.selectedItem.toString()
                                documentUpdate["type"] = false
                            }
                            documentUpdate["amount"] = inputvalue.text.toString().toLong()
                            db.collection("registro")
                                .document(uid)
                                .collection("registropersonal")
                                .document(id).update(documentUpdate).addOnSuccessListener {
                                    toastMessage(getString(R.string.registro_actualizado))
                                }
                            findNavController().navigate(R.id.action_EditarRegistroFragment_to_navigation_registro)
                        } else {
                            toastMessage(getString(R.string.ingresar_nuevo_monto))
                        }

                    }
                    .show()
            }
        }

        return binding.root
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
