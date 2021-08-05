package com.example.myfinances.fragments.sub_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myfinances.databinding.FragmentIngresosBinding

class IngresosFragment : Fragment() {
    private lateinit var ingresosBinding: FragmentIngresosBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ingresosBinding = FragmentIngresosBinding.inflate(inflater, container, false)
        return ingresosBinding.root
    }
}
