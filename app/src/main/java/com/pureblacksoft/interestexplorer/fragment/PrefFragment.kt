package com.pureblacksoft.interestexplorer.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.activity.MainActivity
import com.pureblacksoft.interestexplorer.databinding.FragmentPrefBinding
import com.pureblacksoft.interestexplorer.function.PrefFun
import com.pureblacksoft.interestexplorer.function.StoreFun

class PrefFragment : Fragment(R.layout.fragment_pref) {
    private var _context: Context? = null
    private var _activity: MainActivity? = null
    private var _binding: FragmentPrefBinding? = null

    private val mContext get() = _context!!
    private val mActivity get() = _activity!!
    private val mBinding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)

        _context = requireContext()
        _activity = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrefBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //region Toolbar
        mActivity.setToolbar(
            icon = R.drawable.ic_pref,
            title = R.string.Pref_Title,
            button = R.drawable.ic_close,
            tabVisible = false,
            searchVisible = false
        )
        //endregion

        //region BottomNav
        mActivity.setBottomNav(navVisible = false)
        //endregion

        //region Theme
        when (PrefFun.currentThemeId) {
            PrefFun.ID_THEME_DEFAULT -> mBinding.rdgThemePF.check(mBinding.rdbDefaultThemePF.id)
            PrefFun.ID_THEME_LIGHT -> mBinding.rdgThemePF.check(mBinding.rdbLightThemePF.id)
            PrefFun.ID_THEME_DARK -> mBinding.rdgThemePF.check(mBinding.rdbDarkThemePF.id)
        }

        mBinding.rdgThemePF.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                mBinding.rdbDefaultThemePF.id -> changeTheme(PrefFun.ID_THEME_DEFAULT)
                mBinding.rdbLightThemePF.id -> changeTheme(PrefFun.ID_THEME_LIGHT)
                mBinding.rdbDarkThemePF.id -> changeTheme(PrefFun.ID_THEME_DARK)
            }
        }
        //endregion

        //region Button
        mActivity.binding.imgToolbarButtonMA.setOnClickListener {
            mActivity.onBackPressed()
        }
        //endregion
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onDetach() {
        super.onDetach()

        _context = null
        _activity = null
    }

    private fun changeTheme(themeId: Int) {
        if (PrefFun.currentThemeId != themeId) {
            mActivity.storeFun.writeInt(StoreFun.KEY_ID_THEME, themeId)
            PrefFun.currentThemeId = themeId
            PrefFun.setAppTheme()
        }
    }
}

//PureBlack Software / Murat BIYIK