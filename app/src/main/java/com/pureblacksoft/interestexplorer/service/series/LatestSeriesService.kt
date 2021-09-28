package com.pureblacksoft.interestexplorer.service.series

import android.content.Context
import android.content.Intent
import android.util.Log

class LatestSeriesService : SeriesService(TAG, onSuccess, onFailure) {
    companion object {
        private const val TAG = "LatestSeriesService"

        private const val SERVICE_ID = 22
        private const val URL_PARAMS = "?order=2"

        var seriesList = mutableListOf<Any>()

        var onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, LatestSeriesService::class.java, SERVICE_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.i(TAG, "onHandleWork: Running")

        requestSeries(URL_PARAMS, seriesList)
    }
}

//PureBlack Software / Murat BIYIK