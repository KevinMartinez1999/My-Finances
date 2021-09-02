package com.example.myfinances.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.example.myfinances.R
import com.example.myfinances.databinding.FragmentRegistroBinding
import com.example.myfinances.fragments.sub_fragments.DialogRegistroFragment
import com.example.myfinances.ui.SectionsRegistroPagerAdapter
import com.google.android.material.tabs.TabLayout

class RegistroFragment : Fragment() {
    private var _binding: FragmentRegistroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sectionsPagerAdapter = SectionsRegistroPagerAdapter(this, childFragmentManager)
        val viewPager: ViewPager = view.findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = view.findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        binding.floatingActionButton.setOnClickListener {
            val newFragment = DialogRegistroFragment()
            activity?.let { it1 -> newFragment.show(it1.supportFragmentManager, "dialog") }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
