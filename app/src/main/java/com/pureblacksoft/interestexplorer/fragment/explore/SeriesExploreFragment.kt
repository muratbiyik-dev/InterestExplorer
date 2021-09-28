package com.pureblacksoft.interestexplorer.fragment.explore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.pureblacksoft.interestexplorer.NavMainDirections
import com.pureblacksoft.interestexplorer.service.filter.SeriesFilterService
import com.pureblacksoft.interestexplorer.service.series.AllSeriesService
import com.pureblacksoft.interestexplorer.service.series.ChoiceSeriesService
import com.pureblacksoft.interestexplorer.service.series.LatestSeriesService

class SeriesExploreFragment : ExploreFragment(TAG) {
    companion object {
        private const val TAG = "SeriesExploreFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //region All Series
        val allPair = getInterestEvent(
            scopeId = ID_SCOPE_ALL,
            interestList = AllSeriesService.seriesList,
            progressView = mBinding.prgAllEF,
            buttonView = mBinding.txtAllTryEF,
            recyclerView = mBinding.rcyAllEF
        )
        AllSeriesService.onSuccess = allPair.first
        AllSeriesService.onFailure = allPair.second
        //endregion

        //region Latest Series
        val latestPair = getInterestEvent(
            scopeId = ID_SCOPE_LATEST,
            interestList = LatestSeriesService.seriesList,
            progressView = mBinding.prgLatestEF,
            buttonView = mBinding.txtLatestTryEF,
            recyclerView = mBinding.rcyLatestEF
        )
        LatestSeriesService.onSuccess = latestPair.first
        LatestSeriesService.onFailure = latestPair.second
        //endregion

        //region Choice Series
        val choicePair = getInterestEvent(
            scopeId = ID_SCOPE_CHOICE,
            interestList = ChoiceSeriesService.seriesList,
            progressView = mBinding.prgChoiceEF,
            buttonView = mBinding.txtChoiceTryEF,
            recyclerView = mBinding.rcyChoiceEF
        )
        ChoiceSeriesService.onSuccess = choicePair.first
        ChoiceSeriesService.onFailure = choicePair.second
        //endregion

        //region Series Filters
        val filterPair = getFilterEvent(
            SeriesFilterService.studioFilterList,
            SeriesFilterService.genreFilterList,
            SeriesFilterService.listFilterList
        )
        SeriesFilterService.onSuccess = filterPair.first
        SeriesFilterService.onFailure = filterPair.second
        //endregion
    }

    override fun navigateListFraagment() {
        val action = NavMainDirections.actionGlobalSeriesListFragment()
        findNavController().navigate(action)
    }

    override fun startInterestService(scopeId: Int) {
        Log.i(TAG, "startInterestService $scopeId: Running")

        when (scopeId) {
            ID_SCOPE_ALL -> {
                mBinding.prgAllEF.visibility = View.VISIBLE

                val intent = Intent(mContext, AllSeriesService::class.java)
                AllSeriesService.enqueueWork(mContext, intent)
            }
            ID_SCOPE_LATEST -> {
                mBinding.prgLatestEF.visibility = View.VISIBLE

                val intent = Intent(mContext, LatestSeriesService::class.java)
                LatestSeriesService.enqueueWork(mContext, intent)
            }
            ID_SCOPE_CHOICE -> {
                mBinding.prgChoiceEF.visibility = View.VISIBLE

                val intent = Intent(mContext, ChoiceSeriesService::class.java)
                ChoiceSeriesService.enqueueWork(mContext, intent)
            }
        }
    }

    override fun startFilterService() {
        Log.i(TAG, "startFilterService: Running")

        val intent = Intent(mContext, SeriesFilterService::class.java)
        SeriesFilterService.enqueueWork(mContext, intent)
    }
}

//PureBlack Software / Murat BIYIK