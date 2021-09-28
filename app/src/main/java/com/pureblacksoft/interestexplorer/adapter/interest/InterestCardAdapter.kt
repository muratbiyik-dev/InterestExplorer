package com.pureblacksoft.interestexplorer.adapter.interest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pureblacksoft.interestexplorer.NavMainDirections
import com.pureblacksoft.interestexplorer.data.Game
import com.pureblacksoft.interestexplorer.data.Movie
import com.pureblacksoft.interestexplorer.data.Novel
import com.pureblacksoft.interestexplorer.data.Series
import com.pureblacksoft.interestexplorer.databinding.PartInterestCardBinding
import com.pureblacksoft.interestexplorer.fragment.detail.GameDetailFragment
import com.pureblacksoft.interestexplorer.fragment.detail.MovieDetailFragment
import com.pureblacksoft.interestexplorer.fragment.detail.NovelDetailFragment
import com.pureblacksoft.interestexplorer.fragment.detail.SeriesDetailFragment

class InterestCardAdapter(private val interestList: MutableList<Any>) :
    RecyclerView.Adapter<InterestCardAdapter.ViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val layoutInflater = LayoutInflater.from(context)
        val binding = PartInterestCardBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(interestList[position])
    }

    override fun getItemCount(): Int = interestList.size

    inner class ViewHolder(private val binding: PartInterestCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(interest: Any) {
            fun setEssentialDetail(
                name: String,
                year: String,
                genre: String,
                creator: String,
                image: String
            ) {
                binding.txtNameICP.text = name
                binding.txtYearICP.text = year
                binding.txtGenreICP.text = genre
                binding.txtCreatorICP.text = creator
                Glide.with(context).load(image).into(binding.imgInterestICP)
            }

            fun setOptionalDetail(rating: String? = null) {
                if (rating != null) {
                    binding.txtRatingICP.visibility = View.VISIBLE
                    binding.txtRatingICP.text = rating
                } else {
                    binding.txtRatingICP.visibility = View.GONE
                }
            }

            when (interest) {
                is Movie -> {
                    setEssentialDetail(
                        name = interest.name,
                        year = interest.releaseYear,
                        genre = interest.genreName,
                        creator = interest.studioName,
                        image = interest.imageURL
                    )
                    setOptionalDetail(rating = interest.rating)

                    binding.root.setOnClickListener {
                        MovieDetailFragment.accessedMovie = interestList[adapterPosition] as Movie

                        val action = NavMainDirections.actionGlobalMovieDetailFragment()
                        it.findNavController().navigate(action)
                    }
                }
                is Series -> {
                    setEssentialDetail(
                        name = interest.name,
                        year = interest.firstEpisodeYear + interest.finalEpisodeYear,
                        genre = interest.genreName,
                        creator = interest.studioName,
                        image = interest.imageURL
                    )
                    setOptionalDetail(rating = interest.rating)

                    binding.root.setOnClickListener {
                        SeriesDetailFragment.accessedSeries =
                            interestList[adapterPosition] as Series

                        val action = NavMainDirections.actionGlobalSeriesDetailFragment()
                        it.findNavController().navigate(action)
                    }
                }
                is Game -> {
                    setEssentialDetail(
                        name = interest.name,
                        year = interest.releaseYear,
                        genre = interest.genreName,
                        creator = interest.studioName,
                        image = interest.imageURL
                    )
                    setOptionalDetail(rating = interest.rating)

                    binding.root.setOnClickListener {
                        GameDetailFragment.accessedGame = interestList[adapterPosition] as Game

                        val action = NavMainDirections.actionGlobalGameDetailFragment()
                        it.findNavController().navigate(action)
                    }
                }
                is Novel -> {
                    setEssentialDetail(
                        name = interest.name,
                        year = interest.publishYear,
                        genre = interest.genreName,
                        creator = interest.authorName,
                        image = interest.imageURL
                    )
                    setOptionalDetail()

                    binding.root.setOnClickListener {
                        NovelDetailFragment.accessedNovel = interestList[adapterPosition] as Novel

                        val action = NavMainDirections.actionGlobalNovelDetailFragment()
                        it.findNavController().navigate(action)
                    }
                }
            }
        }
    }
}

//PureBlack Software / Murat BIYIK