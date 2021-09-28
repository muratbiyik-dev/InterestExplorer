package com.pureblacksoft.interestexplorer.fragment.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.adapter.interest.InterestCardAdapter
import com.pureblacksoft.interestexplorer.databinding.FragmentSearchBinding
import com.pureblacksoft.interestexplorer.service.SearchService

class SearchFragment : MainFragment(R.layout.fragment_search) {
    companion object {
        private const val TAG = "SearchFragment"
    }

    private var _binding: FragmentSearchBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var interestCardAdapter: InterestCardAdapter
    private var searchText: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //region Toolbar
        mActivity.setToolbar(
            icon = R.drawable.ic_search,
            title = R.string.Search_Title,
            tabVisible = false,
            searchVisible = true
        )
        //endregion

        //region BottomNav
        mActivity.setBottomNav(navVisible = true)
        //endregion

        //region RecyclerView
        val linearManager = LinearLayoutManager(mContext)
        linearManager.orientation = LinearLayoutManager.VERTICAL

        val itmDecoration = DividerItemDecoration(mContext, linearManager.orientation)
        ContextCompat.getDrawable(mContext, R.drawable.shape_divider)
            ?.let { itmDecoration.setDrawable(it) }

        mBinding.rcyInterestSF.apply {
            layoutManager = linearManager
            addItemDecoration(itmDecoration)
        }

        setInterestCardAdapter()
        //endregion

        //region Search
        mActivity.binding.searchMA.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                SearchService.interestList.clear()
                setInterestCardAdapter()

                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchText = query
                    startSearchService(query)
                }

                mActivity.binding.searchMA.clearFocus()

                return true
            }
        })

        mActivity.binding.searchMA.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                mBinding.lnrInfoSF.visibility = View.GONE
                mBinding.txtNoResultSF.visibility = View.GONE
                mBinding.txtTryAgainSF.visibility = View.GONE
            } else if (interestCardAdapter.itemCount == 0 && mBinding.prgLoadingSF.visibility == View.GONE) {
                mBinding.lnrInfoSF.visibility = View.VISIBLE
            }
        }

        if (searchText != null) {
            mBinding.lnrInfoSF.visibility = View.GONE
        }
        //endregion

        //region Event
        SearchService.onSuccess = {
            mBinding.prgLoadingSF.visibility = View.GONE

            setInterestCardAdapter()

            if (interestCardAdapter.itemCount == 0) {
                mBinding.txtNoResultSF.visibility = View.VISIBLE
            }
        }

        SearchService.onFailure = {
            mBinding.prgLoadingSF.visibility = View.GONE
            mBinding.txtTryAgainSF.visibility = View.VISIBLE
            mBinding.txtTryAgainSF.setOnClickListener {
                mBinding.txtTryAgainSF.visibility = View.GONE

                searchText?.let { startSearchService(it) }
            }
        }
        //endregion
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun setInterestCardAdapter() {
        Log.i(TAG, "setInterestCardAdapter: Running")

        interestCardAdapter = InterestCardAdapter(SearchService.interestList)
        mBinding.rcyInterestSF.adapter = interestCardAdapter
    }

    private fun startSearchService(query: String) {
        Log.i(TAG, "startSearchService: Running")

        mBinding.prgLoadingSF.visibility = View.VISIBLE

        val intent = Intent(mContext, SearchService::class.java)
        intent.putExtra("query", query)
        SearchService.enqueueWork(mContext, intent)
    }
}

//PureBlack Software / Murat BIYIK