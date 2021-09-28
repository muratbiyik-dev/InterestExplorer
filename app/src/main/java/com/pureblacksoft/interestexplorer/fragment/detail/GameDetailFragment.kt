package com.pureblacksoft.interestexplorer.fragment.detail

import android.os.Bundle
import android.view.View
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.data.Game
import com.pureblacksoft.interestexplorer.service.filter.GameFilterService

class GameDetailFragment : DetailFragment(TAG) {
    companion object {
        private const val TAG = "GameDetailFragment"

        lateinit var accessedGame: Game
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentDetailId = ID_DETAIL_GAME

        //region Toolbar
        mActivity.setToolbar(icon = R.drawable.ic_game, title = R.string.Game_Title)
        //endregion

        //region Game
        setEssentialDetail(
            name = accessedGame.name,
            year = accessedGame.releaseYear,
            genre = accessedGame.genreName,
            creator = accessedGame.studioName,
            description = accessedGame.description,
            image = accessedGame.imageURL
        )
        setOptionalDetailLabel()
        setOptionalDetail(rating = accessedGame.rating, video = accessedGame.videoURL)
        //endregion

        //region Recommend
        val genre = GameFilterService.genreFilterList.find { it.name == accessedGame.genreName }
        if (genre != null) startRecommendService(genre.id)
        //endregion
    }
}

//PureBlack Software / Murat BIYIK