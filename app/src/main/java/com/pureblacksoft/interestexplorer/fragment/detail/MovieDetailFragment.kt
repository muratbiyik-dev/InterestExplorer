package com.pureblacksoft.interestexplorer.fragment.detail

import android.os.Bundle
import android.view.View
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.data.Movie
import com.pureblacksoft.interestexplorer.service.filter.MovieFilterService

class MovieDetailFragment : DetailFragment(TAG) {
    companion object {
        private const val TAG = "MovieDetailFragment"

        lateinit var accessedMovie: Movie
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentDetailId = ID_DETAIL_MOVIE

        //region Toolbar
        mActivity.setToolbar(icon = R.drawable.ic_movie, title = R.string.Movie_Title)
        //endregion

        //region Movie
        setEssentialDetail(
            name = accessedMovie.name,
            year = accessedMovie.releaseYear,
            genre = accessedMovie.genreName,
            creator = accessedMovie.studioName,
            description = accessedMovie.description,
            image = accessedMovie.imageURL
        )
        setOptionalDetailLabel(label1 = R.string.Detail_Running_Time_Label)
        setOptionalDetail(
            rating = accessedMovie.rating,
            detail1 = accessedMovie.runningTime,
            video = accessedMovie.videoURL
        )
        //endregion

        //region Recommend
        val genre = MovieFilterService.genreFilterList.find { it.name == accessedMovie.genreName }
        if (genre != null) startRecommendService(genre.id)
        //endregion
    }
}

//PureBlack Software / Murat BIYIK