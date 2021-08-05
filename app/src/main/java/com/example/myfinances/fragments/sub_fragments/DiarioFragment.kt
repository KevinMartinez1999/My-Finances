package com.example.myfinances.fragments.sub_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myfinances.databinding.FragmentDiarioBinding

class DiarioFragment : Fragment() {
    private lateinit var diarioBinding: FragmentDiarioBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        diarioBinding = FragmentDiarioBinding.inflate(inflater, container, false)
        return diarioBinding.root
    }
}
