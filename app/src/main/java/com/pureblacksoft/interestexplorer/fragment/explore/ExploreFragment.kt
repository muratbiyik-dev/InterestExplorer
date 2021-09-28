package com.pureblacksoft.interestexplorer.fragment.explore

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.ybq.android.spinkit.SpinKitView
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.activity.MainActivity
import com.pureblacksoft.interestexplorer.adapter.interest.InterestCoverAdapter
import com.pureblacksoft.interestexplorer.data.manage.InterestFilter
import com.pureblacksoft.interestexplorer.databinding.FragmentExploreBinding
import com.pureblacksoft.interestexplorer.fragment.list.ListFragment

abstract class ExploreFragment(private val TAG: String) : Fragment(R.layout.fragment_explore) {
    companion object {
        const val ID_SCOPE_ALL = 0
        const val ID_SCOPE_LATEST = 1
        const val ID_SCOPE_CHOICE = 2

        var currentScopeId = 0
    }

    private var _context: Context? = null
    private var _activity: MainActivity? = null
    private var _binding: FragmentExploreBinding? = null

    protected val mContext get() = _context!!
    protected val mActivity get() = _activity!!
    protected val mBinding get() = _binding!!

    private lateinit var interestCoverAdapter: InterestCoverAdapter

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
        _binding = FragmentExploreBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //region RecyclerView
        fun setRecyclerView(recyclerView: RecyclerView) {
            val linearManager = LinearLayoutManager(mContext)
            linearManager.orientation = LinearLayoutManager.HORIZONTAL

            val itmDecoration = DividerItemDecoration(mContext, linearManager.orientation)
            ContextCompat.getDrawable(mContext, R.drawable.shape_divider_cover)
                ?.let { itmDecoration.setDrawable(it) }

            recyclerView.apply {
                layoutManager = linearManager
                addItemDecoration(itmDecoration)
                isNestedScrollingEnabled = false
            }
        }

        setRecyclerView(mBinding.rcyAllEF)
        setRecyclerView(mBinding.rcyLatestEF)
        setRecyclerView(mBinding.rcyChoiceEF)
        //endregion

        //region Button
        fun setListFragment(scopeId: Int) {
            currentScopeId = scopeId
            ListFragment.currentCount = 0
            ListFragment.totalCount = 0
        }

        mBinding.imgAllButtonEF.setOnClickListener {
            setListFragment(ID_SCOPE_ALL)
            navigateListFraagment()
        }

        mBinding.imgLatestButtonEF.setOnClickListener {
            setListFragment(ID_SCOPE_LATEST)
            navigateListFraagment()
        }

        mBinding.imgChoiceButtonEF.setOnClickListener {
            setListFragment(ID_SCOPE_CHOICE)
            navigateListFraagment()
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

    protected abstract fun navigateListFraagment()

    protected abstract fun startInterestService(scopeId: Int)

    protected abstract fun startFilterService()

    protected fun getInterestEvent(
        scopeId: Int,
        interestList: MutableList<Any>,
        progressView: SpinKitView,
        buttonView: TextView,
        recyclerView: RecyclerView,
    ): Pair<(() -> Unit)?, (() -> Unit)?> {
        Log.i(TAG, "getInterestEvent $scopeId: Running")

        var onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null

        if (interestList.isEmpty()) {
            startInterestService(scopeId)

            onSuccess = {
                progressView.visibility = View.GONE

                setInterestCoverAdapter(interestList, recyclerView)
            }

            onFailure = {
                progressView.visibility = View.GONE
                buttonView.visibility = View.VISIBLE
                buttonView.setOnClickListener {
                    buttonView.visibility = View.GONE

                    startInterestService(scopeId)
                }
            }
        } else {
            setInterestCoverAdapter(interestList, recyclerView)
        }

        return Pair(onSuccess, onFailure)
    }

    private fun setInterestCoverAdapter(
        interestList: MutableList<Any>,
        recyclerView: RecyclerView
    ) {
        Log.i(TAG, "setInterestCoverAdapter: Running")

        interestCoverAdapter = InterestCoverAdapter(interestList)
        recyclerView.adapter = interestCoverAdapter
    }

    protected fun getFilterEvent(
        creatorFilterList: MutableList<InterestFilter>,
        genreFilterList: MutableList<InterestFilter>,
        listFilterList: MutableList<InterestFilter>
    ): Pair<(() -> Unit)?, (() -> Unit)?> {
        Log.i(TAG, "getFilterEvent: Running")

        val onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null

        fun clearFilters() {
            Log.i(TAG, "clearFilters: Running")

            creatorFilterList.clear()
            genreFilterList.clear()
            listFilterList.clear()
        }

        if (creatorFilterList.isEmpty() || genreFilterList.isEmpty() || listFilterList.isEmpty()) {
            clearFilters()

            startFilterService()

            onFailure = {
                clearFilters()
            }
        }

        return Pair(onSuccess, onFailure)
    }
}

//PureBlack Software / Murat BIYIK