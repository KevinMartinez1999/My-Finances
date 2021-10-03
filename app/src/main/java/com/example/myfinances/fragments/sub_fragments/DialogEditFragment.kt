package com.example.myfinances.fragments.sub_fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.myfinances.R
import com.example.myfinances.databinding.FragmentDialogEditBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DialogEditFragment : DialogFragment() {

    private var _binding: FragmentDialogEditBinding? = null
    private val binding get() = _binding!!
    private val args: DialogEditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout to use as dialog or embedded fragment
        _binding = FragmentDialogEditBinding.inflate(inflater, container, false)

        val registro = args.register
        val type: String = if (registro?.type == true) {
            getString(R.string.ingresos).uppercase()
        } else {
            getString(R.string.gastos).uppercase()
        }

        val description = type + " DEL " + registro?.date.toString()
        binding.texttitle.text = description
        val id: String = args.register?.id.toString()
        with(binding) {
            val uid = Firebase.auth.currentUser?.uid.toString()
            val db = Firebase.firestore
            buttonBorrar.setOnClickListener {
                db.collection("registro")
                    .document(uid)
                    .collection("registropersonal")
                    .document(id).delete()
                Toast.makeText(requireContext(), "Registro Eliminado", Toast.LENGTH_SHORT).show()
            }

            buttonEditar.setOnClickListener {
                val documentUpdate = HashMap<String, Any>()
                if (binding.value.text.isNotEmpty()) {
                    documentUpdate["amount"] = binding.value.text.toString().toLong()
                    db.collection("registro")
                        .document(uid)
                        .collection("registropersonal")
                        .document(id).update(documentUpdate).addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
                                "Registro Actualizado con Exito",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Ingrese el nuevo monto del registro",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return binding.root
    }

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
