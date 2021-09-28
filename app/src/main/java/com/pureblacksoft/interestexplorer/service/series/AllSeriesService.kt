package com.pureblacksoft.interestexplorer.service.series

import android.content.Context
import android.content.Intent
import android.util.Log

class AllSeriesService : SeriesService(TAG, onSuccess, onFailure) {
    companion object {
        private const val TAG = "AllSeriesService"

        private const val SERVICE_ID = 21
        private const val URL_PARAMS = ""

        var seriesList = mutableListOf<Any>()

        var onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, AllSeriesService::class.java, SERVICE_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.i(TAG, "onHandleWork: Running")

        requestSeries(URL_PARAMS, seriesList)
    }
}

//PureBlack Software / Murat BIYIK