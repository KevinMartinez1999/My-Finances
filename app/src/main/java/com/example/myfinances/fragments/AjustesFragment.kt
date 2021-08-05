package com.example.myfinances.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
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

        /*
        ajustesBinding.darkTheme.setOnClickListener {
            if (ajustesBinding.darkTheme.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }*/

        return ajustesBinding.root
    }
}
