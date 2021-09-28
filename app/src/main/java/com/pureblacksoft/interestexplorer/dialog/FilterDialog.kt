package com.pureblacksoft.interestexplorer.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.adapter.manage.FilterAdapter
import com.pureblacksoft.interestexplorer.data.manage.InterestFilter
import com.pureblacksoft.interestexplorer.databinding.DialogFilterBinding
import com.pureblacksoft.interestexplorer.fragment.list.ListFragment
import com.pureblacksoft.interestexplorer.service.filter.GameFilterService
import com.pureblacksoft.interestexplorer.service.filter.MovieFilterService
import com.pureblacksoft.interestexplorer.service.filter.NovelFilterService
import com.pureblacksoft.interestexplorer.service.filter.SeriesFilterService

class FilterDialog(context: Context) : Dialog(context), AdapterView.OnItemSelectedListener {
    companion object {
        var currentCreatorId = 0
        var currentGenreId = 0
        var currentListId = 0

        var onCancel: (() -> Unit)? = null
        var onApply: (() -> Unit)? = null
    }

    private lateinit var binding: DialogFilterBinding
    private var creatorFilterList = mutableListOf<InterestFilter>()
    private var genreFilterList = mutableListOf<InterestFilter>()
    private var listFilterList = mutableListOf<InterestFilter>()
    private var selectedCreatorId = 0
    private var selectedGenreId = 0
    private var selectedListId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //region Interest Control
        fun setInterestFilter(
            creatorTitle: Int,
            creators: MutableList<InterestFilter>,
            genres: MutableList<InterestFilter>,
            lists: MutableList<InterestFilter>
        ) {
            binding.txtCreatorTitleFD.text = context.getString(creatorTitle)
            binding.spnCreatorFD.prompt = context.getString(creatorTitle)

            creatorFilterList = creators
            genreFilterList = genres
            listFilterList = lists
        }

        when (ListFragment.currentListId) {
            ListFragment.ID_LIST_MOVIE -> {
                setInterestFilter(
                    creatorTitle = R.string.Filter_Studio_Title,
                    creators = MovieFilterService.studioFilterList,
                    genres = MovieFilterService.genreFilterList,
                    lists = MovieFilterService.listFilterList
                )
            }
            ListFragment.ID_LIST_SERIES -> {
                setInterestFilter(
                    creatorTitle = R.string.Filter_Creator_Title,
                    creators = SeriesFilterService.studioFilterList,
                    genres = SeriesFilterService.genreFilterList,
                    lists = SeriesFilterService.listFilterList
                )
            }
            ListFragment.ID_LIST_GAME -> {
                setInterestFilter(
                    creatorTitle = R.string.Filter_Studio_Title,
                    creators = GameFilterService.studioFilterList,
                    genres = GameFilterService.genreFilterList,
                    lists = GameFilterService.listFilterList
                )
            }
            ListFragment.ID_LIST_NOVEL -> {
                setInterestFilter(
                    creatorTitle = R.string.Filter_Author_Title,
                    creators = NovelFilterService.authorFilterList,
                    genres = NovelFilterService.genreFilterList,
                    lists = NovelFilterService.listFilterList
                )
            }
        }
        //endregion

        //region Spinner
        binding.spnCreatorFD.onItemSelectedListener = this
        val creatorAdapter = FilterAdapter(context, creatorFilterList)
        binding.spnCreatorFD.adapter = creatorAdapter

        binding.spnGenreFD.onItemSelectedListener = this
        val genreAdapter = FilterAdapter(context, genreFilterList)
        binding.spnGenreFD.adapter = genreAdapter

        binding.spnListFD.onItemSelectedListener = this
        val listAdapter = FilterAdapter(context, listFilterList)
        binding.spnListFD.adapter = listAdapter
        //endregion

        //region Button
        binding.txtCreatorFD.setOnClickListener {
            binding.spnCreatorFD.performClick()
        }

        binding.txtGenreFD.setOnClickListener {
            binding.spnGenreFD.performClick()
        }

        binding.txtListFD.setOnClickListener {
            binding.spnListFD.performClick()
        }

        binding.txtCancelFD.setOnClickListener {
            onCancel?.invoke()
        }

        binding.txtApplyFD.setOnClickListener {
            currentCreatorId = selectedCreatorId
            currentGenreId = selectedGenreId
            currentListId = selectedListId

            onApply?.invoke()
        }
        //endregion
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedCreator = creatorFilterList[binding.spnCreatorFD.selectedItemPosition]
        selectedCreatorId = selectedCreator.id
        binding.txtCreatorFD.text = selectedCreator.name

        val selectedGenre = genreFilterList[binding.spnGenreFD.selectedItemPosition]
        selectedGenreId = selectedGenre.id
        binding.txtGenreFD.text = selectedGenre.name

        val selectedList = listFilterList[binding.spnListFD.selectedItemPosition]
        selectedListId = selectedList.id
        binding.txtListFD.text = selectedList.name
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun show() {
        super.show()

        val creatorPosition = creatorFilterList.indexOfFirst { it.id == currentCreatorId }
        val genrePosition = genreFilterList.indexOfFirst { it.id == currentGenreId }
        val listPosition = listFilterList.indexOfFirst { it.id == currentListId }

        binding.spnCreatorFD.setSelection(creatorPosition)
        binding.spnGenreFD.setSelection(genrePosition)
        binding.spnListFD.setSelection(listPosition)
    }
}

//PureBlack Software / Murat BIYIK