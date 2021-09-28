package com.pureblacksoft.interestexplorer.service.series

import android.content.Context
import android.content.Intent
import android.util.Log
import com.pureblacksoft.interestexplorer.dialog.FilterDialog
import com.pureblacksoft.interestexplorer.dialog.OrderDialog
import com.pureblacksoft.interestexplorer.fragment.list.ListFragment

class ListSeriesService : SeriesService(TAG, onSuccess, onFailure) {
    companion object {
        private const val TAG = "ListSeriesService"

        private const val SERVICE_ID = 20

        var seriesList = mutableListOf<Any>()

        var onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, ListSeriesService::class.java, SERVICE_ID, intent)
        }
    }

    private val urlParams = "?studioId=${FilterDialog.currentCreatorId}" +
            "&genreId=${FilterDialog.currentGenreId}" +
            "&listId=${FilterDialog.currentListId}" +
            "&order=${OrderDialog.currentOrder}" +
            "&page=${ListFragment.currentPage}"

    override fun onHandleWork(intent: Intent) {
        Log.i(TAG, "onHandleWork: Running")

        requestSeries(urlParams, seriesList)
    }
}

//PureBlack Software / Murat BIYIK