package com.pureblacksoft.interestexplorer.fragment.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.service.game.ListGameService

class GameListFragment : ListFragment(TAG) {
    companion object {
        private const val TAG = "GameListFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentListId = ID_LIST_GAME

        //region Toolbar
        mActivity.setToolbar(icon = R.drawable.ic_game, title = R.string.List_Game)
        //endregion

        //region Games
        val interestPair = getInterestEvent(ListGameService.gameList)
        ListGameService.onSuccess = interestPair.first
        ListGameService.onFailure = interestPair.second

        setInterestCardAdapter(ListGameService.gameList)
        //endregion
    }

    override fun startInterestService() {
        Log.i(TAG, "startInterestService: Running")

        mBinding.prgLoadingLF.visibility = View.VISIBLE

        val intent = Intent(mContext, ListGameService::class.java)
        ListGameService.enqueueWork(mContext, intent)
    }

    override fun resetInterestList() {
        ListGameService.gameList.clear()
        setInterestCardAdapter(ListGameService.gameList)

        startInterestService()
    }
}

//PureBlack Software / Murat BIYIK