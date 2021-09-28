package com.pureblacksoft.interestexplorer.fragment.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.service.novel.ListNovelService

class NovelListFragment : ListFragment(TAG) {
    companion object {
        private const val TAG = "NovelListFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentListId = ID_LIST_NOVEL

        //region Toolbar
        mActivity.setToolbar(icon = R.drawable.ic_novel, title = R.string.List_Novel)
        //endregion

        //region Novels
        val interestPair = getInterestEvent(ListNovelService.novelList)
        ListNovelService.onSuccess = interestPair.first
        ListNovelService.onFailure = interestPair.second

        setInterestCardAdapter(ListNovelService.novelList)
        //endregion
    }

    override fun startInterestService() {
        Log.i(TAG, "startInterestService: Running")

        mBinding.prgLoadingLF.visibility = View.VISIBLE

        val intent = Intent(mContext, ListNovelService::class.java)
        ListNovelService.enqueueWork(mContext, intent)
    }

    override fun resetInterestList() {
        ListNovelService.novelList.clear()
        setInterestCardAdapter(ListNovelService.novelList)

        startInterestService()
    }
}

//PureBlack Software / Murat BIYIK