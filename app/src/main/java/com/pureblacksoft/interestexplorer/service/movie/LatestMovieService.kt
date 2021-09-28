package com.pureblacksoft.interestexplorer.service.movie

import android.content.Context
import android.content.Intent
import android.util.Log

class LatestMovieService : MovieService(TAG, onSuccess, onFailure) {
    companion object {
        private const val TAG = "LatestMovieService"

        private const val SERVICE_ID = 12
        private const val URL_PARAMS = "?order=2"

        var movieList = mutableListOf<Any>()

        var onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, LatestMovieService::class.java, SERVICE_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.i(TAG, "onHandleWork: Running")

        requestMovie(URL_PARAMS, movieList)
    }
}

//PureBlack Software / Murat BIYIK