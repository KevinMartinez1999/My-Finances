package com.example.myfinances.fragments.sub_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myfinances.databinding.FragmentMensualBinding

class MensualFragment : Fragment() {
    private lateinit var mensualBinding: FragmentMensualBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mensualBinding = FragmentMensualBinding.inflate(inflater, container, false)
        return mensualBinding.root
    }
}
