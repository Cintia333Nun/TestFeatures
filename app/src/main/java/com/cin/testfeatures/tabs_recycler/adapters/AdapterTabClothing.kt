package com.cin.testfeatures.tabs_recycler.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.cin.testfeatures.tabs_recycler.datasource.TypeClothes
import com.cin.testfeatures.tabs_recycler.view.ErrorFragment
import com.cin.testfeatures.tabs_recycler.view.ClotheFragment

class AdapterTabClothing(fragmentManager: FragmentManager):
    FragmentPagerAdapter(fragmentManager) {
    companion object {
        const val NUMBER_TABS = 3
    }

    override fun getCount(): Int = NUMBER_TABS

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ClotheFragment(TypeClothes.SHIRT)
            1 -> ClotheFragment(TypeClothes.JEANS)
            2 -> ClotheFragment(TypeClothes.DRESS)
            else -> ErrorFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when(position) {
            0 -> "Playeras"
            1 -> "Pantalones"
            2 -> "Vestidos"
            else -> ""
        }
    }
}