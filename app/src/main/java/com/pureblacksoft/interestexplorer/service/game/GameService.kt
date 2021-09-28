package com.pureblacksoft.interestexplorer.service.game

import android.util.Log
import androidx.core.app.JobIntentService
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pureblacksoft.interestexplorer.activity.MainActivity
import com.pureblacksoft.interestexplorer.data.Game
import com.pureblacksoft.interestexplorer.fragment.list.ListFragment
import org.json.JSONException
import org.json.JSONObject

abstract class GameService(
    private val TAG: String,
    private val onSuccess: (() -> Unit)?,
    private val onFailure: (() -> Unit)?
) : JobIntentService() {
    companion object {
        const val URL_GAME = MainActivity.URL_INTEREST_EXPLORER + "script/db_json_game.php"
        private const val URL_GAME_IMAGE = MainActivity.URL_INTEREST_EXPLORER + "image/game/"

        fun getGameJSON(TAG: String, response: JSONObject, gameList: MutableList<Any>) {
            val gameArray = response.getJSONArray("gameArray")
            val gaLength = gameArray.length()
            for (i in 0 until gaLength) {
                val gameObject = gameArray.getJSONObject(i)
                val game = Game(
                    id = gameObject.getString("gameId").toInt(),
                    name = gameObject.getString("gameName"),
                    description = gameObject.getString("description"),
                    releaseYear = gameObject.getString("releaseYear"),
                    rating = gameObject.getString("rating").toFloat().toString(),
                    imageURL = URL_GAME_IMAGE + gameObject.getString("imageURL"),
                    videoURL = gameObject.getString("videoURL"),
                    studioName = gameObject.getString("studioName"),
                    genreName = gameObject.getString("genreName"),
                    listName = gameObject.getString("listName")
                )
                gameList.add(game)

                Log.i(TAG, "Game ${game.id}: Added")
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

    protected fun requestGame(urlParams: String, gameList: MutableList<Any>) {
        Log.i(TAG, "requestGame: Running")

        val dataURL = URL_GAME + urlParams
        val jsonRequest = JsonObjectRequest(Request.Method.GET, dataURL, null,
            { response ->
                Log.i(TAG, "Connection successful: $dataURL")

                try {
                    val gameStatus = response.getJSONObject("gameStatus")
                    ListFragment.totalCount = gameStatus.getString("gameCount").toInt()

                    getGameJSON(TAG, response, gameList)

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