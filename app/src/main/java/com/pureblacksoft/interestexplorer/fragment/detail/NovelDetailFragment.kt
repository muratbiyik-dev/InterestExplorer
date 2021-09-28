package com.pureblacksoft.interestexplorer.fragment.detail

import android.os.Bundle
import android.view.View
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.data.Novel
import com.pureblacksoft.interestexplorer.service.filter.NovelFilterService

class NovelDetailFragment : DetailFragment(TAG) {
    companion object {
        private const val TAG = "NovelDetailFragment"

        lateinit var accessedNovel: Novel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentDetailId = ID_DETAIL_NOVEL

        //region Toolbar
        mActivity.setToolbar(icon = R.drawable.ic_novel, title = R.string.Novel_Title)
        //endregion

        //region Novel
        setEssentialDetail(
            name = accessedNovel.name,
            year = accessedNovel.publishYear,
            genre = accessedNovel.genreName,
            creator = accessedNovel.authorName,
            description = accessedNovel.description,
            image = accessedNovel.imageURL
        )
        setOptionalDetailLabel(label1 = R.string.Detail_Page_Count_Label)
        setOptionalDetail(detail1 = accessedNovel.pageCount)
        //endregion

        //region Recommend
        val genre = NovelFilterService.genreFilterList.find { it.name == accessedNovel.genreName }
        if (genre != null) startRecommendService(genre.id)
        //endregion
    }
}

//PureBlack Software / Murat BIYIK