package com.example.myfinances.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.myfinances.R
import com.example.myfinances.data.server.RegistroServer
import com.example.myfinances.databinding.FragmentBienesBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.DecimalFormat

class BienesFragment : Fragment() {

    private var _binding: FragmentBienesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBienesBinding.inflate(inflater, container, false)

        loadFromServer()

        return binding.root
    }

    private fun loadFromServer() {
        val uid = Firebase.auth.currentUser?.uid.toString()
        val db = Firebase.firestore
        db.collection("registro")
            .document(uid)
            .collection("registropersonal")
            .get()
            .addOnSuccessListener { result ->
                var sumaCuenta: Long = 0
                var sumaEfectivo: Long = 0
                var sumaTarjeta: Long = 0
                for (document in result) {
                    val registro: RegistroServer = document.toObject()
                    if (registro.account == "Efectivo") {
                        if (registro.type == true) {
                            sumaEfectivo += registro.amount!!
                        } else {
                            sumaEfectivo -= registro.amount!!
                        }
                    } else if (registro.account == "Cuenta Bancaria") {
                        if (registro.type == true) {
                            sumaCuenta += registro.amount!!
                        } else {
                            sumaCuenta -= registro.amount!!
                        }
                    } else if (registro.account == "Tarjeta de Crédito") {
                        if (registro.type == true) {
                            sumaTarjeta += registro.amount!!
                        } else {
                            sumaTarjeta -= registro.amount!!
                        }
                    }
                }

                val balance: Long = sumaEfectivo + sumaCuenta + sumaTarjeta
                var capital: Long = 0
                var aDeber: Long = 0

                if (sumaEfectivo > 0) {
                    capital += sumaEfectivo
                } else {
                    aDeber += sumaEfectivo
                }

                if (sumaCuenta > 0) {
                    capital += sumaCuenta
                } else {
                    aDeber += sumaCuenta
                }

                if (sumaTarjeta > 0) {
                    capital += sumaTarjeta
                } else {
                    aDeber += sumaTarjeta
                }

                mostrarEnPantalla(
                    sumaCuenta,
                    sumaEfectivo,
                    sumaTarjeta,
                    balance,
                    capital,
                    aDeber
                )
            }
    }

    private fun ponerSigno(numero: Long, view: TextView) {
        val context: Context = binding.root.context
        val dec = DecimalFormat("###,###,###,###,###,###,###,###.##")
        if (numero >= 0) {
            val num = dec.format(numero)
            view.setTextColor(Color.BLUE)
            view.text =
                context.getString(R.string.positive, num)
        } else {
            val num = dec.format(kotlin.math.abs(numero))
            view.setTextColor(Color.RED)
            view.text =
                context.getString(R.string.negative, num)
        }
    }

    private fun mostrarEnPantalla(
        sumaCuenta: Long,
        sumaEfectivo: Long,
        sumaTarjeta: Long,
        balance: Long,
        capital: Long,
        aDeber: Long
    ) {

        ponerSigno(sumaCuenta, binding.valorCuenta)
        ponerSigno(sumaEfectivo, binding.valorEfectivo)
        ponerSigno(sumaTarjeta, binding.valorTarjeta)
        ponerSigno(balance, binding.valueBalance)
        ponerSigno(capital, binding.valueCapital)
        ponerSigno(aDeber, binding.valueDeber)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
