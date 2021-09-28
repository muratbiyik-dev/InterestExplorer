package com.pureblacksoft.interestexplorer.fragment.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pureblacksoft.interestexplorer.R
import com.pureblacksoft.interestexplorer.activity.MainActivity
import com.pureblacksoft.interestexplorer.adapter.interest.InterestCoverAdapter
import com.pureblacksoft.interestexplorer.databinding.FragmentDetailBinding
import com.pureblacksoft.interestexplorer.service.RecommendService

abstract class DetailFragment(private val TAG: String) : Fragment(R.layout.fragment_detail) {
    companion object {
        const val ID_DETAIL_MOVIE = 1
        const val ID_DETAIL_SERIES = 2
        const val ID_DETAIL_GAME = 3
        const val ID_DETAIL_NOVEL = 4

        var currentDetailId = 0
    }

    private var _context: Context? = null
    private var _activity: MainActivity? = null
    private var _binding: FragmentDetailBinding? = null

    protected val mContext get() = _context!!
    protected val mActivity get() = _activity!!
    protected val mBinding get() = _binding!!

    private lateinit var interestCoverAdapter: InterestCoverAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        _context = requireContext()
        _activity = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //region Toolbar
        mActivity.setToolbar(
            button = R.drawable.ic_close,
            tabVisible = false,
            searchVisible = false
        )
        //endregion

        //region BottomNav
        mActivity.setBottomNav(navVisible = false)
        //endregion

        //region RecyclerView
        val linearManager = LinearLayoutManager(mContext)
        linearManager.orientation = LinearLayoutManager.HORIZONTAL

        val itmDecoration = DividerItemDecoration(mContext, linearManager.orientation)
        ContextCompat.getDrawable(mContext, R.drawable.shape_divider_cover)
            ?.let { itmDecoration.setDrawable(it) }

        mBinding.rcyRecommendDF.apply {
            layoutManager = linearManager
            addItemDecoration(itmDecoration)
            isNestedScrollingEnabled = false
        }
        //endregion

        //region Event
        RecommendService.onSuccess = {
            mBinding.prgRecommendDF.visibility = View.GONE

            setInterestCoverAdapter()
        }

        RecommendService.onFailure = {
            mBinding.prgRecommendDF.visibility = View.GONE
            mBinding.txtRecommendTryDF.visibility = View.VISIBLE
            mBinding.txtRecommendTryDF.setOnClickListener {
                mBinding.txtRecommendTryDF.visibility = View.GONE

                setInterestCoverAdapter()
            }
        }
        //endregion

        //region Button
        mActivity.binding.imgToolbarButtonMA.setOnClickListener {
            mActivity.onBackPressed()
        }
        //endregion

        //region Observer
        lifecycle.addObserver(mBinding.ytpInterestDF)
        //endregion
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onDetach() {
        super.onDetach()

        _context = null
        _activity = null
    }

    protected fun setEssentialDetail(
        name: String,
        year: String,
        genre: String,
        creator: String,
        description: String,
        image: String
    ) {
        mBinding.txtNameDF.text = name
        mBinding.txtYearDF.text = year
        mBinding.txtGenreDF.text = genre
        mBinding.txtCreatorDF.text = creator
        mBinding.txtDescriptionDF.text = description
        Glide.with(mContext).load(image).into(mBinding.imgInterestDF)
    }

    protected fun setOptionalDetailLabel(label1: Int? = null, label2: Int? = null) {
        if (label1 != null) {
            mBinding.lnrDetailLine1DF.visibility = View.VISIBLE
            mBinding.txtDetailLabel1DF.text = getString(label1)
        } else {
            mBinding.lnrDetailLine1DF.visibility = View.GONE
        }

        if (label2 != null) {
            mBinding.lnrDetailLine2DF.visibility = View.VISIBLE
            mBinding.txtDetailLabel2DF.text = getString(label2)
        } else {
            mBinding.lnrDetailLine2DF.visibility = View.GONE
        }
    }

    protected fun setOptionalDetail(
        rating: String? = null,
        detail1: String? = null,
        detail2: String? = null,
        video: String? = null
    ) {
        if (rating != null) {
            mBinding.txtRatingDF.visibility = View.VISIBLE
            mBinding.txtRatingDF.text = rating
        } else {
            mBinding.txtRatingDF.visibility = View.GONE
        }

        if (detail1 != null) {
            mBinding.txtDetail1DF.text = detail1
        }

        if (detail2 != null) {
            mBinding.txtDetail2DF.text = detail2
        }

        if (video != null) {
            mBinding.ytpInterestDF.visibility = View.VISIBLE
            mBinding.ytpInterestDF.addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(video, 0F)
                }
            })
        } else {
            mBinding.ytpInterestDF.visibility = View.GONE
        }
    }

    protected fun startRecommendService(genreId: Int) {
        Log.i(TAG, "startRecommendService: Running")

        mBinding.prgRecommendDF.visibility = View.VISIBLE

        val intent = Intent(mContext, RecommendService::class.java)
        intent.putExtra("genreId", genreId.toString())
        RecommendService.enqueueWork(mContext, intent)
    }

    private fun setInterestCoverAdapter() {
        Log.i(TAG, "setInterestCoverAdapter: Running")

        interestCoverAdapter = InterestCoverAdapter(RecommendService.recommendList)
        mBinding.rcyRecommendDF.adapter = interestCoverAdapter
    }
}

//PureBlack Software / Murat BIYIK