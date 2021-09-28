package com.pureblacksoft.interestexplorer.service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pureblacksoft.interestexplorer.fragment.detail.*
import com.pureblacksoft.interestexplorer.service.game.GameService
import com.pureblacksoft.interestexplorer.service.movie.MovieService
import com.pureblacksoft.interestexplorer.service.novel.NovelService
import com.pureblacksoft.interestexplorer.service.series.SeriesService
import org.json.JSONException

class RecommendService : JobIntentService() {
    companion object {
        private const val TAG = "RecommendService"

        private const val SERVICE_ID = 99

        var recommendList = mutableListOf<Any>()

        var onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, RecommendService::class.java, SERVICE_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.i(TAG, "onHandleWork: Running")

        val genreId = intent.getStringExtra("genreId")

        if (genreId != null) {
            requestRecommend(genreId.toInt())
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

    private fun requestRecommend(genreId: Int) {
        Log.i(TAG, "requestRecommend: Running")

        var dataURL = when (DetailFragment.currentDetailId) {
            DetailFragment.ID_DETAIL_MOVIE -> MovieService.URL_MOVIE
            DetailFragment.ID_DETAIL_SERIES -> SeriesService.URL_SERIES
            DetailFragment.ID_DETAIL_GAME -> GameService.URL_GAME
            DetailFragment.ID_DETAIL_NOVEL -> NovelService.URL_NOVEL
            else -> ""
        }
        dataURL = "$dataURL?genreId=$genreId&order=99"

        val jsonRequest = JsonObjectRequest(Request.Method.GET, dataURL, null,
            { response ->
                Log.i(TAG, "Connection successful: $dataURL")

                recommendList.clear()

                try {
                    when (DetailFragment.currentDetailId) {
                        DetailFragment.ID_DETAIL_MOVIE -> {
                            MovieService.getMovieJSON(TAG, response, recommendList)
                            recommendList.remove(MovieDetailFragment.accessedMovie)
                        }
                        DetailFragment.ID_DETAIL_SERIES -> {
                            SeriesService.getSeriesJSON(TAG, response, recommendList)
                            recommendList.remove(SeriesDetailFragment.accessedSeries)
                        }
                        DetailFragment.ID_DETAIL_GAME -> {
                            GameService.getGameJSON(TAG, response, recommendList)
                            recommendList.remove(GameDetailFragment.accessedGame)
                        }
                        DetailFragment.ID_DETAIL_NOVEL -> {
                            NovelService.getNovelJSON(TAG, response, recommendList)
                            recommendList.remove(NovelDetailFragment.accessedNovel)
                        }
                        else -> onFailure?.invoke()
                    }

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