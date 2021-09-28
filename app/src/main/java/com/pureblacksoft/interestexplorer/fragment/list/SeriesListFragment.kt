package com.pureblacksoft.interestexplorer.fragment.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.service.series.ListSeriesService

class SeriesListFragment : ListFragment(TAG) {
    companion object {
        private const val TAG = "SeriesListFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentListId = ID_LIST_SERIES

        //region Toolbar
        mActivity.setToolbar(icon = R.drawable.ic_series, title = R.string.List_Series)
        //endregion

        //region Series
        val interestPair = getInterestEvent(ListSeriesService.seriesList)
        ListSeriesService.onSuccess = interestPair.first
        ListSeriesService.onFailure = interestPair.second

        setInterestCardAdapter(ListSeriesService.seriesList)
        //endregion
    }

    override fun startInterestService() {
        Log.i(TAG, "startInterestService: Running")

        mBinding.prgLoadingLF.visibility = View.VISIBLE

        val intent = Intent(mContext, ListSeriesService::class.java)
        ListSeriesService.enqueueWork(mContext, intent)
    }

    override fun resetInterestList() {
        ListSeriesService.seriesList.clear()
        setInterestCardAdapter(ListSeriesService.seriesList)

        startInterestService()
    }
}

//PureBlack Software / Murat BIYIK