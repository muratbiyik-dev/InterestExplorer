package com.pureblacksoft.interestexplorer.fragment.explore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.pureblacksoft.interestexplorer.NavMainDirections
import com.pureblacksoft.interestexplorer.service.filter.GameFilterService
import com.pureblacksoft.interestexplorer.service.game.AllGameService
import com.pureblacksoft.interestexplorer.service.game.ChoiceGameService
import com.pureblacksoft.interestexplorer.service.game.LatestGameService

class GameExploreFragment : ExploreFragment(TAG) {
    companion object {
        private const val TAG = "GameExploreFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //region All Games
        val allPair = getInterestEvent(
            scopeId = ID_SCOPE_ALL,
            interestList = AllGameService.gameList,
            progressView = mBinding.prgAllEF,
            buttonView = mBinding.txtAllTryEF,
            recyclerView = mBinding.rcyAllEF
        )
        AllGameService.onSuccess = allPair.first
        AllGameService.onFailure = allPair.second
        //endregion

        //region Latest Games
        val latestPair = getInterestEvent(
            scopeId = ID_SCOPE_LATEST,
            interestList = LatestGameService.gameList,
            progressView = mBinding.prgLatestEF,
            buttonView = mBinding.txtLatestTryEF,
            recyclerView = mBinding.rcyLatestEF
        )
        LatestGameService.onSuccess = latestPair.first
        LatestGameService.onFailure = latestPair.second
        //endregion

        //region Choice Games
        val choicePair = getInterestEvent(
            scopeId = ID_SCOPE_CHOICE,
            interestList = ChoiceGameService.gameList,
            progressView = mBinding.prgChoiceEF,
            buttonView = mBinding.txtChoiceTryEF,
            recyclerView = mBinding.rcyChoiceEF
        )
        ChoiceGameService.onSuccess = choicePair.first
        ChoiceGameService.onFailure = choicePair.second
        //endregion

        //region Game Filters
        val filterPair = getFilterEvent(
            GameFilterService.studioFilterList,
            GameFilterService.genreFilterList,
            GameFilterService.listFilterList
        )
        GameFilterService.onSuccess = filterPair.first
        GameFilterService.onFailure = filterPair.second
        //endregion
    }

    override fun navigateListFraagment() {
        val action = NavMainDirections.actionGlobalGameListFragment()
        findNavController().navigate(action)
    }

    override fun startInterestService(scopeId: Int) {
        Log.i(TAG, "startInterestService $scopeId: Running")

        when (scopeId) {
            ID_SCOPE_ALL -> {
                mBinding.prgAllEF.visibility = View.VISIBLE

                val intent = Intent(mContext, AllGameService::class.java)
                AllGameService.enqueueWork(mContext, intent)
            }
            ID_SCOPE_LATEST -> {
                mBinding.prgLatestEF.visibility = View.VISIBLE

                val intent = Intent(mContext, LatestGameService::class.java)
                LatestGameService.enqueueWork(mContext, intent)
            }
            ID_SCOPE_CHOICE -> {
                mBinding.prgChoiceEF.visibility = View.VISIBLE

                val intent = Intent(mContext, ChoiceGameService::class.java)
                ChoiceGameService.enqueueWork(mContext, intent)
            }
        }
    }

    override fun startFilterService() {
        Log.i(TAG, "startFilterService: Running")

        val intent = Intent(mContext, GameFilterService::class.java)
        GameFilterService.enqueueWork(mContext, intent)
    }
}

//PureBlack Software / Murat BIYIK