package com.pureblacksoft.interestexplorer.service.filter

import android.content.Context
import android.content.Intent
import android.util.Log
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.data.manage.InterestFilter
import com.pureblacksoft.interestexplorer.fragment.list.ListFragment

class NovelFilterService : FilterService(TAG, onSuccess, onFailure) {
    companion object {
        private const val TAG = "NovelFilterService"

        private const val SERVICE_ID = 411

        var authorFilterList = mutableListOf<InterestFilter>()
        var genreFilterList = mutableListOf<InterestFilter>()
        var listFilterList = mutableListOf<InterestFilter>()

        var onSuccess: (() -> Unit)? = null
        var onFailure: (() -> Unit)? = null

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(context, NovelFilterService::class.java, SERVICE_ID, intent)
        }
    }

    override fun onHandleWork(intent: Intent) {
        Log.i(TAG, "onHandleWork: Running")

        authorFilterList.add(InterestFilter(0, getString(R.string.Filter_Author_All)))
        genreFilterList.add(InterestFilter(0, getString(R.string.Filter_Genre_All)))
        listFilterList.add(InterestFilter(0, getString(R.string.Filter_List_All)))

        requestFilter(
            ListFragment.ID_LIST_NOVEL,
            authorFilterList,
            genreFilterList,
            listFilterList
        )
    }
}

//PureBlack Software / Murat BIYIK