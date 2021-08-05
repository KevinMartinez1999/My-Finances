package com.example.myfinances.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myfinances.databinding.FragmentAjustesBinding

class AjustesFragment : Fragment() {
    private lateinit var ajustesBinding: FragmentAjustesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ajustesBinding = FragmentAjustesBinding.inflate(inflater, container, false)
        return ajustesBinding.root
    }
}
