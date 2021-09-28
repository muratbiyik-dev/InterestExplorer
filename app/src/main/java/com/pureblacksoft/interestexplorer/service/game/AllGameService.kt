package com.pureblacksoft.interestexplorer.service.game

import android.content.Context
import android.content.Intent
import android.util.Log

class AllGameService : GameService(TAG, onSuccess, onFailure) {
    companion object {
        private const val TAG = "AllGameService"

        private const val SERVICE_ID = 31
        private const val URL_PARAMS = ""

        var gameList = mutableListOf<Any>()

        var onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, AllGameService::class.java, SERVICE_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.i(TAG, "onHandleWork: Running")

        requestGame(URL_PARAMS, gameList)
    }
}

//PureBlack Software / Murat BIYIK