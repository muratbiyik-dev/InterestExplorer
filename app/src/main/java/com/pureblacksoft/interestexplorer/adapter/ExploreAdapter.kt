package com.pureblacksoft.interestexplorer.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pureblacksoft.interestexplorer.fragment.explore.*

class ExploreAdapter(fragment: Fragment, private val tlSize: Int) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> SeriesExploreFragment()
            2 -> GameExploreFragment()
            3 -> NovelExploreFragment()
            else -> MovieExploreFragment()
        }
    }

    override fun getItemCount(): Int = tlSize
}

//PureBlack Software / Murat BIYIK