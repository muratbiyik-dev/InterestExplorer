package com.pureblacksoft.interestexplorer.service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pureblacksoft.interestexplorer.activity.MainActivity
import com.pureblacksoft.interestexplorer.service.game.GameService
import com.pureblacksoft.interestexplorer.service.movie.MovieService
import com.pureblacksoft.interestexplorer.service.novel.NovelService
import com.pureblacksoft.interestexplorer.service.series.SeriesService
import org.json.JSONException

class SearchService : JobIntentService() {
    companion object {
        private const val TAG = "SearchService"

        private const val SERVICE_ID = 1
        private const val URL_SEARCH =
            MainActivity.URL_INTEREST_EXPLORER + "script/db_json_search.php"

        var interestList = mutableListOf<Any>()

        var onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, SearchService::class.java, SERVICE_ID, intent)
        }
    }

    override fun onCreate() {
        super.onCreate()

        Log.i(TAG, "onCreate: Running")
    }

    override fun onHandleWork(intent: Intent) {
        Log.i(TAG, "onHandleWork: Running")

        val query = intent.getStringExtra("query")

        if (query != null) {
            requestSearch(query)
        } else {
            onFailure?.invoke()
        }
    }

    override fun onStopCurrentWork(): Boolean {
        Log.i(TAG, "onStopCurrentWork: Running")

        return super.onStopCurrentWork()
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.i(TAG, "onDestroy: Running")
    }

    private fun requestSearch(query: String) {
        Log.i(TAG, "requestSearch: Running")

        val dataURL = "$URL_SEARCH?query='$query*'"
        val jsonRequest = JsonObjectRequest(Request.Method.GET, dataURL, null,
            { response ->
                Log.i(TAG, "Connection successful: $dataURL")

                try {
                    MovieService.getMovieJSON(TAG, response, interestList)
                    SeriesService.getSeriesJSON(TAG, response, interestList)
                    GameService.getGameJSON(TAG, response, interestList)
                    NovelService.getNovelJSON(TAG, response, interestList)

                    onSuccess?.invoke()
                } catch (e: JSONException) {
                    Log.e(TAG, e.toString())

                    onFailure?.invoke()
                }
            },
            { error ->
                Log.i(TAG, "Connection failed: $dataURL")
                Log.e(TAG, error.toString())

                onFailure?.invoke()
            })

        Volley.newRequestQueue(this).add(jsonRequest)
    }
}

//PureBlack Software / Murat BIYIK