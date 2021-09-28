package com.pureblacksoft.interestexplorer.service.series

import android.util.Log
import androidx.core.app.JobIntentService
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pureblacksoft.interestexplorer.activity.MainActivity
import com.pureblacksoft.interestexplorer.data.Series
import com.pureblacksoft.interestexplorer.fragment.list.ListFragment
import org.json.JSONException
import org.json.JSONObject

abstract class SeriesService(
    private val TAG: String,
    private val onSuccess: (() -> Unit)?,
    private val onFailure: (() -> Unit)?
) : JobIntentService() {
    companion object {
        const val URL_SERIES = MainActivity.URL_INTEREST_EXPLORER + "script/db_json_series.php"
        private const val URL_SERIES_IMAGE = MainActivity.URL_INTEREST_EXPLORER + "image/series/"

        fun getSeriesJSON(TAG: String, response: JSONObject, seriesList: MutableList<Any>) {
            val seriesArray = response.getJSONArray("seriesArray")
            val saLength = seriesArray.length()
            for (i in 0 until saLength) {
                val seriesObject = seriesArray.getJSONObject(i)
                val series = Series(
                    id = seriesObject.getString("seriesId").toInt(),
                    name = seriesObject.getString("seriesName"),
                    description = seriesObject.getString("description"),
                    firstEpisodeYear = seriesObject.getString("firstEpisodeYear"),
                    finalEpisodeYear =
                    if (seriesObject.getString("finalEpisodeYear") != "null")
                        " - " + seriesObject.getString("finalEpisodeYear")
                    else
                        "",
                    seasonCount = seriesObject.getString("seasonCount"),
                    episodeCount = seriesObject.getString("episodeCount"),
                    rating = seriesObject.getString("rating").toFloat().toString(),
                    imageURL = URL_SERIES_IMAGE + seriesObject.getString("imageURL"),
                    videoURL = seriesObject.getString("videoURL"),
                    studioName = seriesObject.getString("studioName"),
                    genreName = seriesObject.getString("genreName"),
                    listName = seriesObject.getString("listName")
                )
                seriesList.add(series)

                Log.i(TAG, "Series ${series.id}: Added")
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        Log.i(TAG, "onCreate: Running")
    }

    override fun onStopCurrentWork(): Boolean {
        Log.i(TAG, "onStopCurrentWork: Running")

        return super.onStopCurrentWork()
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.i(TAG, "onDestroy: Running")
    }

    protected fun requestSeries(urlParams: String, seriesList: MutableList<Any>) {
        Log.i(TAG, "requestSeries: Running")

        val dataURL = URL_SERIES + urlParams
        val jsonRequest = JsonObjectRequest(Request.Method.GET, dataURL, null,
            { response ->
                Log.i(TAG, "Connection successful: $dataURL")

                try {
                    val seriesStatus = response.getJSONObject("seriesStatus")
                    ListFragment.totalCount = seriesStatus.getString("seriesCount").toInt()

                    getSeriesJSON(TAG, response, seriesList)

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