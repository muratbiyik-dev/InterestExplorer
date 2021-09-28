package com.pureblacksoft.interestexplorer.fragment.main

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.pureblacksoft.interestexplorer.NavMainDirections
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.activity.MainActivity

abstract class MainFragment(fragmentId: Int) : Fragment(fragmentId) {
    private var _context: Context? = null
    private var _activity: MainActivity? = null

    protected val mContext get() = _context!!
    protected val mActivity get() = _activity!!

    override fun onAttach(context: Context) {
        super.onAttach(context)

        _context = requireContext()
        _activity = requireActivity() as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //region Toolbar
        mActivity.setToolbar(button = R.drawable.ic_pref)
        //endregion

        //region BottomNav
        mActivity.binding.bottomNavMA.setupWithNavController(mActivity.navController)
        mActivity.binding.bottomNavMA.itemIconTintList = null

        mActivity.setBottomNav(
            button1 = R.drawable.selector_home,
            button2 = R.drawable.selector_search,
            navVisible = true
        )
        //endregion

        //region Button
        mActivity.binding.imgToolbarButtonMA.setOnClickListener {
            val action = NavMainDirections.actionGlobalPrefFragment()
            findNavController().navigate(action)
        }
        //endregion
    }

    override fun onDetach() {
        super.onDetach()

        _context = null
        _activity = null
    }
}

//PureBlack Software / Murat BIYIK