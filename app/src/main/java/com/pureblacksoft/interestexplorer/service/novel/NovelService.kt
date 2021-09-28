package com.pureblacksoft.interestexplorer.service.novel

import android.util.Log
import androidx.core.app.JobIntentService
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pureblacksoft.interestexplorer.activity.MainActivity
import com.pureblacksoft.interestexplorer.data.Novel
import com.pureblacksoft.interestexplorer.fragment.list.ListFragment
import org.json.JSONException
import org.json.JSONObject

abstract class NovelService(
    private val TAG: String,
    private val onSuccess: (() -> Unit)?,
    private val onFailure: (() -> Unit)?
) : JobIntentService() {
    companion object {
        const val URL_NOVEL = MainActivity.URL_INTEREST_EXPLORER + "script/db_json_novel.php"
        private const val URL_NOVEL_IMAGE = MainActivity.URL_INTEREST_EXPLORER + "image/novel/"

        fun getNovelJSON(TAG: String, response: JSONObject, novelList: MutableList<Any>) {
            val novelArray = response.getJSONArray("novelArray")
            val naLength = novelArray.length()
            for (i in 0 until naLength) {
                val novelObject = novelArray.getJSONObject(i)
                val novel = Novel(
                    id = novelObject.getString("novelId").toInt(),
                    name = novelObject.getString("novelName"),
                    description = novelObject.getString("description"),
                    publishYear = novelObject.getString("publishYear"),
                    pageCount = novelObject.getString("pageCount"),
                    imageURL = URL_NOVEL_IMAGE + novelObject.getString("imageURL"),
                    authorName = novelObject.getString("authorName"),
                    genreName = novelObject.getString("genreName"),
                    listName = novelObject.getString("listName")
                )
                novelList.add(novel)

                Log.i(TAG, "Novel ${novel.id}: Added")
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

    protected fun requestNovel(urlParams: String, novelList: MutableList<Any>) {
        Log.i(TAG, "requestNovel: Running")

        val dataURL = URL_NOVEL + urlParams
        val jsonRequest = JsonObjectRequest(Request.Method.GET, dataURL, null,
            { response ->
                Log.i(TAG, "Connection successful: $dataURL")

                try {
                    val novelStatus = response.getJSONObject("novelStatus")
                    ListFragment.totalCount = novelStatus.getString("novelCount").toInt()

                    getNovelJSON(TAG, response, novelList)

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