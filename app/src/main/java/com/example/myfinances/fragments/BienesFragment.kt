package com.example.myfinances.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myfinances.databinding.FragmentBienesBinding

class BienesFragment : Fragment() {
    private lateinit var bienesBinding: FragmentBienesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bienesBinding = FragmentBienesBinding.inflate(inflater, container, false)
        return bienesBinding.root
    }
}
