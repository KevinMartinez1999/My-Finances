package com.example.myfinances.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myfinances.databinding.FragmentEstadisticasBinding

class EstadisticasFragment : Fragment() {
    private lateinit var estadisticasBinding: FragmentEstadisticasBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        estadisticasBinding = FragmentEstadisticasBinding.inflate(inflater, container, false)
        return estadisticasBinding.root
    }
}
