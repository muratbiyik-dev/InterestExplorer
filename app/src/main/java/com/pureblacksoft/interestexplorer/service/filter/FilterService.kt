package com.pureblacksoft.interestexplorer.service.filter

import android.util.Log
import androidx.core.app.JobIntentService
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.pureblacksoft.interestexplorer.activity.MainActivity
import com.pureblacksoft.interestexplorer.data.manage.InterestFilter
import com.pureblacksoft.interestexplorer.fragment.list.ListFragment
import org.json.JSONException

abstract class FilterService(
    private val TAG: String,
    private val onSuccess: (() -> Unit)?,
    private val onFailure: (() -> Unit)?
) : JobIntentService() {
    companion object {
        private const val URL_FILTER =
            MainActivity.URL_INTEREST_EXPLORER + "script/db_json_filter.php"

        private var nameCreator = "studio"
        private var nameGenre = "genre"
        private var nameList = "list"
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

    protected fun requestFilter(
        interestId: Int,
        creatorFilterList: MutableList<InterestFilter>,
        genreFilterList: MutableList<InterestFilter>,
        listFilterList: MutableList<InterestFilter>
    ) {
        Log.i(TAG, "requestFilter: Running")

        if (interestId == ListFragment.ID_LIST_NOVEL)
            nameCreator = "author"

        val dataURL = "$URL_FILTER?interestId=$interestId"
        val jsonRequest = JsonObjectRequest(Request.Method.GET, dataURL, null,
            { response ->
                Log.i(TAG, "Connection successful: $dataURL")

                fun fillFilterList(filterName: String, filterList: MutableList<InterestFilter>) {
                    val filterArray = response.getJSONArray(filterName + "Array")
                    val faLength = filterArray.length()
                    for (i in 0 until faLength) {
                        val filterObject = filterArray.getJSONObject(i)
                        val filter = InterestFilter(
                            id = filterObject.getString(filterName + "Id").toInt(),
                            name = filterObject.getString(filterName + "Name")
                        )
                        filterList.add(filter)

                        Log.i(TAG, "$filterName Filter ${filter.id}: Added")
                    }
                }

                try {
                    fillFilterList(nameCreator, creatorFilterList)
                    fillFilterList(nameGenre, genreFilterList)
                    fillFilterList(nameList, listFilterList)

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