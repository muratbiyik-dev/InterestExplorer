package com.pureblacksoft.interestexplorer.service.game

import android.content.Context
import android.content.Intent
import android.util.Log

class ChoiceGameService : GameService(TAG, onSuccess, onFailure) {
    companion object {
        private const val TAG = "ChoiceGameService"

        private const val SERVICE_ID = 33
        private const val URL_PARAMS = "?listId=1"

        var gameList = mutableListOf<Any>()

        var onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, ChoiceGameService::class.java, SERVICE_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.i(TAG, "onHandleWork: Running")

        requestGame(URL_PARAMS, gameList)
    }
}

//PureBlack Software / Murat BIYIK