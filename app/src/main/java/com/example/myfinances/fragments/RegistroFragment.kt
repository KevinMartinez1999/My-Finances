package com.example.myfinances.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myfinances.databinding.FragmentRegistroBinding

class RegistroFragment : Fragment() {
    private lateinit var registroBinding: FragmentRegistroBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        registroBinding = FragmentRegistroBinding.inflate(inflater, container, false)
        return registroBinding.root
    }
}
