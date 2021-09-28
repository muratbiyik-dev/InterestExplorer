package com.pureblacksoft.interestexplorer.fragment.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.service.movie.ListMovieService

class MovieListFragment : ListFragment(TAG) {
    companion object {
        private const val TAG = "MovieListFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentListId = ID_LIST_MOVIE

        //region Toolbar
        mActivity.setToolbar(icon = R.drawable.ic_movie, title = R.string.List_Movie)
        //endregion

        //region Movies
        val interestPair = getInterestEvent(ListMovieService.movieList)
        ListMovieService.onSuccess = interestPair.first
        ListMovieService.onFailure = interestPair.second

        setInterestCardAdapter(ListMovieService.movieList)
        //endregion
    }

    override fun startInterestService() {
        Log.i(TAG, "startInterestService: Running")

        mBinding.prgLoadingLF.visibility = View.VISIBLE

        val intent = Intent(mContext, ListMovieService::class.java)
        ListMovieService.enqueueWork(mContext, intent)
    }

    override fun resetInterestList() {
        ListMovieService.movieList.clear()
        setInterestCardAdapter(ListMovieService.movieList)

        startInterestService()
    }
}

//PureBlack Software / Murat BIYIK