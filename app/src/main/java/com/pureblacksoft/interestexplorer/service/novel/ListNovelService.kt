package com.pureblacksoft.interestexplorer.service.novel

import android.content.Context
import android.content.Intent
import android.util.Log
import com.pureblacksoft.interestexplorer.dialog.FilterDialog
import com.pureblacksoft.interestexplorer.dialog.OrderDialog
import com.pureblacksoft.interestexplorer.fragment.list.ListFragment

class ListNovelService : NovelService(TAG, onSuccess, onFailure) {
    companion object {
        private const val TAG = "ListNovelService"

        private const val SERVICE_ID = 40

        var novelList = mutableListOf<Any>()

        var onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, ListNovelService::class.java, SERVICE_ID, intent)
        }
    }

    private val urlParams = "?authorId=${FilterDialog.currentCreatorId}" +
            "&genreId=${FilterDialog.currentGenreId}" +
            "&listId=${FilterDialog.currentListId}" +
            "&order=${OrderDialog.currentOrder}" +
            "&page=${ListFragment.currentPage}"

    override fun onHandleWork(intent: Intent) {
        Log.i(TAG, "onHandleWork: Running")

        requestNovel(urlParams, novelList)
    }
}

//PureBlack Software / Murat BIYIK