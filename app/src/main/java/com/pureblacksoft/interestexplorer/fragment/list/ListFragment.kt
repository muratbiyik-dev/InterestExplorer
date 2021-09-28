package com.pureblacksoft.interestexplorer.fragment.list

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.activity.MainActivity
import com.pureblacksoft.interestexplorer.adapter.interest.InterestCardAdapter
import com.pureblacksoft.interestexplorer.databinding.FragmentListBinding
import com.pureblacksoft.interestexplorer.dialog.FilterDialog
import com.pureblacksoft.interestexplorer.dialog.OrderDialog
import com.pureblacksoft.interestexplorer.fragment.explore.ExploreFragment

abstract class ListFragment(private val TAG: String) : Fragment(R.layout.fragment_list) {
    companion object {
        const val ID_LIST_MOVIE = 1
        const val ID_LIST_SERIES = 2
        const val ID_LIST_GAME = 3
        const val ID_LIST_NOVEL = 4

        var currentListId = 0
        var currentPage = 1
        var currentCount = 0
        var totalCount = 0
    }

    private var _context: Context? = null
    private var _activity: MainActivity? = null
    private var _binding: FragmentListBinding? = null

    protected val mContext get() = _context!!
    protected val mActivity get() = _activity!!
    protected val mBinding get() = _binding!!

    private lateinit var linearManager: LinearLayoutManager
    private lateinit var interestCardAdapter: InterestCardAdapter
    private var currentState: Parcelable? = null

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
        _binding = FragmentListBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //region Toolbar
        mActivity.setToolbar(
            button = R.drawable.ic_close,
            tabVisible = false,
            searchVisible = false
        )
        //endregion

        //region BottomNav
        mActivity.setBottomNav(navVisible = true)
        //endregion

        //region Dialog
        val filterDialog = FilterDialog(mContext)
        val orderDialog = OrderDialog(mContext)
        //endregion

        //region Toast
        val listEndToast = Toast.makeText(mContext, R.string.Toast_List_End, Toast.LENGTH_SHORT)
        listEndToast.setGravity(Gravity.BOTTOM, 0, 160)
        //endregion

        //region RecyclerView
        linearManager = LinearLayoutManager(mContext)
        linearManager.orientation = LinearLayoutManager.VERTICAL

        val itmDecoration = DividerItemDecoration(mContext, linearManager.orientation)
        ContextCompat.getDrawable(mContext, R.drawable.shape_divider)
            ?.let { itmDecoration.setDrawable(it) }

        mBinding.rcyInterestLF.apply {
            layoutManager = linearManager
            addItemDecoration(itmDecoration)
        }

        mBinding.rcyInterestLF.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    if (currentCount < totalCount) {
                        currentPage++

                        saveState()
                        startInterestService()
                    } else {
                        listEndToast.show()
                    }
                }
            }
        })
        //endregion

        //region Scope Control
        fun manageInterest(
            creatorId: Int = 0,
            genreId: Int = 0,
            listId: Int = 0,
            order: Int = 0
        ) {
            Log.i(TAG, "manageInterest: Running")

            FilterDialog.currentCreatorId = creatorId
            FilterDialog.currentGenreId = genreId
            FilterDialog.currentListId = listId
            OrderDialog.currentOrder = order

            checkFilter()
            checkOrder()
        }

        fun onInterestManage() {
            currentPage = 1
            currentState = null

            resetInterestList()
        }

        if (currentCount == 0) {
            when (ExploreFragment.currentScopeId) {
                ExploreFragment.ID_SCOPE_ALL -> manageInterest()
                ExploreFragment.ID_SCOPE_LATEST -> manageInterest(order = 2)
                ExploreFragment.ID_SCOPE_CHOICE -> manageInterest(listId = 1)
            }
            onInterestManage()
        }
        //endregion

        //region Event
        FilterDialog.onApply = {
            filterDialog.dismiss()
            checkFilter()
            onInterestManage()
        }

        FilterDialog.onCancel = {
            filterDialog.dismiss()
        }

        OrderDialog.onApply = {
            orderDialog.dismiss()
            checkOrder()
            onInterestManage()
        }

        OrderDialog.onCancel = {
            orderDialog.dismiss()
        }
        //endregion

        //region Button
        mActivity.binding.imgToolbarButtonMA.setOnClickListener {
            mActivity.onBackPressed()
        }

        mActivity.binding.bottomNavMA.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> filterDialog.show()
                R.id.searchFragment -> orderDialog.show()
            }

            true
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

    protected abstract fun startInterestService()

    protected abstract fun resetInterestList()

    protected fun getInterestEvent(interestList: MutableList<Any>): Pair<(() -> Unit)?, (() -> Unit)?> {
        Log.i(TAG, "getInterestEvent: Running")

        val onSuccess = {
            mBinding.prgLoadingLF.visibility = View.GONE

            setInterestCardAdapter(interestList)
        }

        val onFailure = {
            mBinding.prgLoadingLF.visibility = View.GONE
            mBinding.txtTryAgainLF.visibility = View.VISIBLE
            mBinding.txtTryAgainLF.setOnClickListener {
                mBinding.txtTryAgainLF.visibility = View.GONE

                startInterestService()
            }
        }

        return Pair(onSuccess, onFailure)
    }

    protected fun setInterestCardAdapter(interestList: MutableList<Any>) {
        Log.i(TAG, "setInterestCardAdapter: Running")

        currentCount = interestList.size
        interestCardAdapter = InterestCardAdapter(interestList)
        mBinding.rcyInterestLF.adapter = interestCardAdapter

        restoreState()
    }

    private fun checkFilter() {
        if (FilterDialog.currentCreatorId != 0 ||
            FilterDialog.currentGenreId != 0 ||
            FilterDialog.currentListId != 0
        ) {
            mActivity.setBottomNav(button1 = R.drawable.ic_filter_full)
        } else {
            mActivity.setBottomNav(button1 = R.drawable.ic_filter)
        }
    }

    private fun checkOrder() {
        if (OrderDialog.currentOrder != 0) {
            mActivity.setBottomNav(button2 = R.drawable.ic_order_full)
        } else {
            mActivity.setBottomNav(button2 = R.drawable.ic_order)
        }
    }

    private fun saveState() {
        if (interestCardAdapter.itemCount != 0) {
            currentState = linearManager.onSaveInstanceState()
        }
    }

    private fun restoreState() {
        if (interestCardAdapter.itemCount != 0 && currentState != null) {
            linearManager.onRestoreInstanceState(currentState)
        }
    }
}

//PureBlack Software / Murat BIYIK