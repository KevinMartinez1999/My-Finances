package com.example.myfinances.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.myfinances.R
import com.example.myfinances.fragments.EstadisticasFragment
import com.example.myfinances.fragments.sub_fragments.GastosFragment
import com.example.myfinances.fragments.sub_fragments.IngresosFragment

private val TAB_TITLES = arrayOf(
    R.string.ingresos,
    R.string.gastos
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
@Suppress("DEPRECATION")
class SectionsEstadisticasPagerAdapter(
    private val context: EstadisticasFragment,
    fm: FragmentManager
) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> IngresosFragment()
            else -> GastosFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}
