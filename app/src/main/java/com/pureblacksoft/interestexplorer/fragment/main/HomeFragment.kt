package com.pureblacksoft.interestexplorer.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.adapter.ExploreAdapter
import com.pureblacksoft.interestexplorer.databinding.FragmentHomeBinding

class HomeFragment : MainFragment(R.layout.fragment_home) {
    companion object {
        private const val TAG = "HomeFragment"
    }

    private var _binding: FragmentHomeBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //region Toolbar
        mActivity.setToolbar(
            icon = R.drawable.ic_home,
            title = R.string.Home_Title,
            tabVisible = true,
            searchVisible = false
        )
        //endregion

        //region TabLayout
        val titleList = mutableListOf<String>()
        titleList.add(getString(R.string.Explore_Movie))
        titleList.add(getString(R.string.Explore_Series))
        titleList.add(getString(R.string.Explore_Game))
        titleList.add(getString(R.string.Explore_Novel))

        mBinding.pagerHF.adapter = ExploreAdapter(this, titleList.size)
        TabLayoutMediator(mActivity.binding.tabMA, mBinding.pagerHF) { tab, position ->
            tab.text = titleList[position]
        }.attach()
        mBinding.pagerHF.isUserInputEnabled = false
        //endregion
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}

//PureBlack Software / Murat BIYIK