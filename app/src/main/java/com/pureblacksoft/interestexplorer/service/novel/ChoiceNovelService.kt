package com.pureblacksoft.interestexplorer.service.novel

import android.content.Context
import android.content.Intent
import android.util.Log

class ChoiceNovelService : NovelService(TAG, onSuccess, onFailure) {
    companion object {
        private const val TAG = "ChoiceNovelService"

        private const val SERVICE_ID = 43
        private const val URL_PARAMS = "?listId=1"

        var novelList = mutableListOf<Any>()

        var onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, ChoiceNovelService::class.java, SERVICE_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.i(TAG, "onHandleWork: Running")

        requestNovel(URL_PARAMS, novelList)
    }
}

//PureBlack Software / Murat BIYIK