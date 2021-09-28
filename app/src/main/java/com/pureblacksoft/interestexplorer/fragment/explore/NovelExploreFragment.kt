package com.pureblacksoft.interestexplorer.fragment.explore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.pureblacksoft.interestexplorer.NavMainDirections
import com.pureblacksoft.interestexplorer.service.filter.NovelFilterService
import com.pureblacksoft.interestexplorer.service.novel.AllNovelService
import com.pureblacksoft.interestexplorer.service.novel.ChoiceNovelService
import com.pureblacksoft.interestexplorer.service.novel.LatestNovelService

class NovelExploreFragment : ExploreFragment(TAG) {
    companion object {
        private const val TAG = "NovelExploreFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //region All Novels
        val allPair = getInterestEvent(
            scopeId = ID_SCOPE_ALL,
            interestList = AllNovelService.novelList,
            progressView = mBinding.prgAllEF,
            buttonView = mBinding.txtAllTryEF,
            recyclerView = mBinding.rcyAllEF
        )
        AllNovelService.onSuccess = allPair.first
        AllNovelService.onFailure = allPair.second
        //endregion

        //region Latest Novels
        val latestPair = getInterestEvent(
            scopeId = ID_SCOPE_LATEST,
            interestList = LatestNovelService.novelList,
            progressView = mBinding.prgLatestEF,
            buttonView = mBinding.txtLatestTryEF,
            recyclerView = mBinding.rcyLatestEF
        )
        LatestNovelService.onSuccess = latestPair.first
        LatestNovelService.onFailure = latestPair.second
        //endregion

        //region Choice Novels
        val choicePair = getInterestEvent(
            scopeId = ID_SCOPE_CHOICE,
            interestList = ChoiceNovelService.novelList,
            progressView = mBinding.prgChoiceEF,
            buttonView = mBinding.txtChoiceTryEF,
            recyclerView = mBinding.rcyChoiceEF
        )
        ChoiceNovelService.onSuccess = choicePair.first
        ChoiceNovelService.onFailure = choicePair.second
        //endregion

        //region Novel Filters
        val filterPair = getFilterEvent(
            NovelFilterService.authorFilterList,
            NovelFilterService.genreFilterList,
            NovelFilterService.listFilterList
        )
        NovelFilterService.onSuccess = filterPair.first
        NovelFilterService.onFailure = filterPair.second
        //endregion
    }

    override fun navigateListFraagment() {
        val action = NavMainDirections.actionGlobalNovelListFragment()
        findNavController().navigate(action)
    }

    override fun startInterestService(scopeId: Int) {
        Log.i(TAG, "startInterestService $scopeId: Running")

        when (scopeId) {
            ID_SCOPE_ALL -> {
                mBinding.prgAllEF.visibility = View.VISIBLE

                val intent = Intent(mContext, AllNovelService::class.java)
                AllNovelService.enqueueWork(mContext, intent)
            }
            ID_SCOPE_LATEST -> {
                mBinding.prgLatestEF.visibility = View.VISIBLE

                val intent = Intent(mContext, LatestNovelService::class.java)
                LatestNovelService.enqueueWork(mContext, intent)
            }
            ID_SCOPE_CHOICE -> {
                mBinding.prgChoiceEF.visibility = View.VISIBLE

                val intent = Intent(mContext, ChoiceNovelService::class.java)
                ChoiceNovelService.enqueueWork(mContext, intent)
            }
        }
    }

    override fun startFilterService() {
        Log.i(TAG, "startFilterService: Running")

        val intent = Intent(mContext, NovelFilterService::class.java)
        NovelFilterService.enqueueWork(mContext, intent)
    }
}

//PureBlack Software / Murat BIYIK