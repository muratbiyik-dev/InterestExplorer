package com.pureblacksoft.interestexplorer.adapter.interest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pureblacksoft.interestexplorer.NavMainDirections
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.data.Game
import com.pureblacksoft.interestexplorer.data.Movie
import com.pureblacksoft.interestexplorer.data.Novel
import com.pureblacksoft.interestexplorer.data.Series
import com.pureblacksoft.interestexplorer.databinding.PartInterestCoverBinding
import com.pureblacksoft.interestexplorer.fragment.detail.GameDetailFragment
import com.pureblacksoft.interestexplorer.fragment.detail.MovieDetailFragment
import com.pureblacksoft.interestexplorer.fragment.detail.NovelDetailFragment
import com.pureblacksoft.interestexplorer.fragment.detail.SeriesDetailFragment

class InterestCoverAdapter(private val interestList: MutableList<Any>) :
    RecyclerView.Adapter<InterestCoverAdapter.ViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val layoutInflater = LayoutInflater.from(context)
        val binding = PartInterestCoverBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(interestList[position])
    }

    override fun getItemCount(): Int = interestList.size

    inner class ViewHolder(private val binding: PartInterestCoverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(interest: Any) {
            fun setEssentialDetail(
                year: String,
                image: String
            ) {
                binding.txtYearICP1.text = year
                Glide.with(context).load(image).into(binding.imgInterestICP1)
            }

            fun setOptionalDetail(rating: String? = null) {
                if (rating != null) {
                    binding.txtRatingICP1.visibility = View.VISIBLE
                    binding.txtRatingICP1.text = rating
                } else {
                    binding.txtRatingICP1.visibility = View.GONE
                }
            }

            when (interest) {
                is Movie -> {
                    setEssentialDetail(
                        year = interest.releaseYear,
                        image = interest.imageURL
                    )
                    setOptionalDetail(rating = interest.rating)

                    binding.root.setOnClickListener {
                        MovieDetailFragment.accessedMovie = interestList[adapterPosition] as Movie

                        val action = NavMainDirections.actionGlobalMovieDetailFragment()
                        it.findNavController().popBackStack(R.id.movieDetailFragment, true)
                        it.findNavController().navigate(action)
                    }
                }
                is Series -> {
                    setEssentialDetail(
                        year = interest.firstEpisodeYear,
                        image = interest.imageURL
                    )
                    setOptionalDetail(rating = interest.rating)

                    binding.root.setOnClickListener {
                        SeriesDetailFragment.accessedSeries =
                            interestList[adapterPosition] as Series

                        val action = NavMainDirections.actionGlobalSeriesDetailFragment()
                        it.findNavController().popBackStack(R.id.seriesDetailFragment, true)
                        it.findNavController().navigate(action)
                    }
                }
                is Game -> {
                    setEssentialDetail(
                        year = interest.releaseYear,
                        image = interest.imageURL
                    )
                    setOptionalDetail(rating = interest.rating)

                    binding.root.setOnClickListener {
                        GameDetailFragment.accessedGame = interestList[adapterPosition] as Game

                        val action = NavMainDirections.actionGlobalGameDetailFragment()
                        it.findNavController().popBackStack(R.id.gameDetailFragment, true)
                        it.findNavController().navigate(action)
                    }
                }
                is Novel -> {
                    setEssentialDetail(
                        year = interest.publishYear,
                        image = interest.imageURL
                    )
                    setOptionalDetail()

                    binding.root.setOnClickListener {
                        NovelDetailFragment.accessedNovel = interestList[adapterPosition] as Novel

                        val action = NavMainDirections.actionGlobalNovelDetailFragment()
                        it.findNavController().popBackStack(R.id.novelDetailFragment, true)
                        it.findNavController().navigate(action)
                    }
                }
            }
        }
    }
}

//PureBlack Software / Murat BIYIK