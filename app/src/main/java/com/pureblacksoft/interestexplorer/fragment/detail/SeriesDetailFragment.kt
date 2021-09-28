package com.pureblacksoft.interestexplorer.fragment.detail

import android.os.Bundle
import android.view.View
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.data.Series
import com.pureblacksoft.interestexplorer.service.filter.SeriesFilterService

class SeriesDetailFragment : DetailFragment(TAG) {
    companion object {
        private const val TAG = "SeriesDetailFragment"

        lateinit var accessedSeries: Series
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentDetailId = ID_DETAIL_SERIES

        //region Toolbar
        mActivity.setToolbar(icon = R.drawable.ic_series, title = R.string.Series_Title)
        //endregion

        //region Series
        setEssentialDetail(
            name = accessedSeries.name,
            year = accessedSeries.firstEpisodeYear + accessedSeries.finalEpisodeYear,
            genre = accessedSeries.genreName,
            creator = accessedSeries.studioName,
            description = accessedSeries.description,
            image = accessedSeries.imageURL
        )
        setOptionalDetailLabel(
            label1 = R.string.Detail_Season_Count_Label,
            label2 = R.string.Detail_Episode_Count_Label
        )
        setOptionalDetail(
            rating = accessedSeries.rating,
            detail1 = accessedSeries.seasonCount,
            detail2 = accessedSeries.episodeCount,
            video = accessedSeries.videoURL
        )
        //endregion

        //region Recommend
        val genre = SeriesFilterService.genreFilterList.find { it.name == accessedSeries.genreName }
        if (genre != null) startRecommendService(genre.id)
        //endregion
    }
}

//PureBlack Software / Murat BIYIK