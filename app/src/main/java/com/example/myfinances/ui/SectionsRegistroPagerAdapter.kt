package com.example.myfinances.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.myfinances.R
import com.example.myfinances.fragments.RegistroFragment
import com.example.myfinances.fragments.sub_fragments.DiarioFragment
import com.example.myfinances.fragments.sub_fragments.MensualFragment

private val TAB_TITLES = arrayOf(
    R.string.diario,
    R.string.mensual
)

@Suppress("DEPRECATION")
class SectionsRegistroPagerAdapter(
    private val context: RegistroFragment,
    fm: FragmentManager
) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> DiarioFragment()
            else -> MensualFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}
