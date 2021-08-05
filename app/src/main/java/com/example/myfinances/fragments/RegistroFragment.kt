package com.example.myfinances.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.myfinances.R
import com.example.myfinances.databinding.FragmentRegistroBinding
import com.example.myfinances.ui.SectionsRegistroPagerAdapter
import com.google.android.material.tabs.TabLayout

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sectionsPagerAdapter = SectionsRegistroPagerAdapter(this, childFragmentManager)
        val viewPager: ViewPager = view.findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = view.findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }
}
