package com.pureblacksoft.interestexplorer.fragment.explore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.pureblacksoft.interestexplorer.NavMainDirections
import com.pureblacksoft.interestexplorer.service.filter.MovieFilterService
import com.pureblacksoft.interestexplorer.service.movie.AllMovieService
import com.pureblacksoft.interestexplorer.service.movie.ChoiceMovieService
import com.pureblacksoft.interestexplorer.service.movie.LatestMovieService

class MovieExploreFragment : ExploreFragment(TAG) {
    companion object {
        private const val TAG = "MovieExploreFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //region All Movies
        val allPair = getInterestEvent(
            scopeId = ID_SCOPE_ALL,
            interestList = AllMovieService.movieList,
            progressView = mBinding.prgAllEF,
            buttonView = mBinding.txtAllTryEF,
            recyclerView = mBinding.rcyAllEF
        )
        AllMovieService.onSuccess = allPair.first
        AllMovieService.onFailure = allPair.second
        //endregion

        //region Latest Movies
        val latestPair = getInterestEvent(
            scopeId = ID_SCOPE_LATEST,
            interestList = LatestMovieService.movieList,
            progressView = mBinding.prgLatestEF,
            buttonView = mBinding.txtLatestTryEF,
            recyclerView = mBinding.rcyLatestEF
        )
        LatestMovieService.onSuccess = latestPair.first
        LatestMovieService.onFailure = latestPair.second
        //endregion

        //region Choice Movies
        val choicePair = getInterestEvent(
            scopeId = ID_SCOPE_CHOICE,
            interestList = ChoiceMovieService.movieList,
            progressView = mBinding.prgChoiceEF,
            buttonView = mBinding.txtChoiceTryEF,
            recyclerView = mBinding.rcyChoiceEF
        )
        ChoiceMovieService.onSuccess = choicePair.first
        ChoiceMovieService.onFailure = choicePair.second
        //endregion

        //region Movie Filters
        val filterPair = getFilterEvent(
            MovieFilterService.studioFilterList,
            MovieFilterService.genreFilterList,
            MovieFilterService.listFilterList
        )
        MovieFilterService.onSuccess = filterPair.first
        MovieFilterService.onFailure = filterPair.second
        //endregion
    }

    override fun navigateListFraagment() {
        val action = NavMainDirections.actionGlobalMovieListFragment()
        findNavController().navigate(action)
    }

    override fun startInterestService(scopeId: Int) {
        Log.i(TAG, "startInterestService $scopeId: Running")

        when (scopeId) {
            ID_SCOPE_ALL -> {
                mBinding.prgAllEF.visibility = View.VISIBLE

                val intent = Intent(mContext, AllMovieService::class.java)
                AllMovieService.enqueueWork(mContext, intent)
            }
            ID_SCOPE_LATEST -> {
                mBinding.prgLatestEF.visibility = View.VISIBLE

                val intent = Intent(mContext, LatestMovieService::class.java)
                LatestMovieService.enqueueWork(mContext, intent)
            }
            ID_SCOPE_CHOICE -> {
                mBinding.prgChoiceEF.visibility = View.VISIBLE

                val intent = Intent(mContext, ChoiceMovieService::class.java)
                ChoiceMovieService.enqueueWork(mContext, intent)
            }
        }
    }

    override fun startFilterService() {
        Log.i(TAG, "startFilterService: Running")

        val intent = Intent(mContext, MovieFilterService::class.java)
        MovieFilterService.enqueueWork(mContext, intent)
    }
}

//PureBlack Software / Murat BIYIK