package com.pureblacksoft.interestexplorer.service.game

import android.content.Context
import android.content.Intent
import android.util.Log
import com.pureblacksoft.interestexplorer.dialog.FilterDialog
import com.pureblacksoft.interestexplorer.dialog.OrderDialog
import com.pureblacksoft.interestexplorer.fragment.list.ListFragment

class ListGameService : GameService(TAG, onSuccess, onFailure) {
    companion object {
        private const val TAG = "ListGameService"

        private const val SERVICE_ID = 30

        var gameList = mutableListOf<Any>()

        var onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, ListGameService::class.java, SERVICE_ID, intent)
        }
    }

    private val urlParams = "?studioId=${FilterDialog.currentCreatorId}" +
            "&genreId=${FilterDialog.currentGenreId}" +
            "&listId=${FilterDialog.currentListId}" +
            "&order=${OrderDialog.currentOrder}" +
            "&page=${ListFragment.currentPage}"

    override fun onHandleWork(intent: Intent) {
        Log.i(TAG, "onHandleWork: Running")

        requestGame(urlParams, gameList)
    }
}

//PureBlack Software / Murat BIYIK