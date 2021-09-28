package com.pureblacksoft.interestexplorer.service.movie

import android.util.Log
import androidx.core.app.JobIntentService
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pureblacksoft.interestexplorer.activity.MainActivity
import com.pureblacksoft.interestexplorer.data.Movie
import com.pureblacksoft.interestexplorer.fragment.list.ListFragment
import org.json.JSONException
import org.json.JSONObject

abstract class MovieService(
    private val TAG: String,
    private val onSuccess: (() -> Unit)?,
    private val onFailure: (() -> Unit)?
) : JobIntentService() {
    companion object {
        const val URL_MOVIE = MainActivity.URL_INTEREST_EXPLORER + "script/db_json_movie.php"
        private const val URL_MOVIE_IMAGE = MainActivity.URL_INTEREST_EXPLORER + "image/movie/"

        fun getMovieJSON(TAG: String, response: JSONObject, movieList: MutableList<Any>) {
            val movieArray = response.getJSONArray("movieArray")
            val maLength = movieArray.length()
            for (i in 0 until maLength) {
                val movieObject = movieArray.getJSONObject(i)
                val movie = Movie(
                    id = movieObject.getString("movieId").toInt(),
                    name = movieObject.getString("movieName"),
                    description = movieObject.getString("description"),
                    releaseYear = movieObject.getString("releaseYear"),
                    runningTime = movieObject.getString("runningTime"),
                    rating = movieObject.getString("rating").toFloat().toString(),
                    imageURL = URL_MOVIE_IMAGE + movieObject.getString("imageURL"),
                    videoURL = movieObject.getString("videoURL"),
                    studioName = movieObject.getString("studioName"),
                    genreName = movieObject.getString("genreName"),
                    listName = movieObject.getString("listName")
                )
                movieList.add(movie)

                Log.i(TAG, "Movie ${movie.id}: Added")
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

    protected fun requestMovie(urlParams: String, movieList: MutableList<Any>) {
        Log.i(TAG, "requestMovie: Running")

        val dataURL = URL_MOVIE + urlParams
        val jsonRequest = JsonObjectRequest(Request.Method.GET, dataURL, null,
            { response ->
                Log.i(TAG, "Connection successful: $dataURL")

                try {
                    val movieStatus = response.getJSONObject("movieStatus")
                    ListFragment.totalCount = movieStatus.getString("movieCount").toInt()

                    getMovieJSON(TAG, response, movieList)

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