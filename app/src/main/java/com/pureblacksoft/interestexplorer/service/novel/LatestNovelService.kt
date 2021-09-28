package com.pureblacksoft.interestexplorer.service.novel

import android.content.Context
import android.content.Intent
import android.util.Log

class LatestNovelService : NovelService(TAG, onSuccess, onFailure) {
    companion object {
        private const val TAG = "LatestNovelService"

        private const val SERVICE_ID = 42
        private const val URL_PARAMS = "?order=2"

        var novelList = mutableListOf<Any>()

        var onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, LatestNovelService::class.java, SERVICE_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.i(TAG, "onHandleWork: Running")

        requestNovel(URL_PARAMS, novelList)
    }
}

//PureBlack Software / Murat BIYIK