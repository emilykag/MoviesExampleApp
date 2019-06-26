package com.example.moviesapp.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide

import com.example.moviesapp.R
import com.example.moviesapp.api.Status
import com.example.moviesapp.db.entities.Genre
import com.example.moviesapp.db.entities.ShowType
import com.example.moviesapp.db.entities.Video
import com.example.moviesapp.di.Injectable
import com.example.moviesapp.util.extensions.showToast
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject
import android.content.Intent
import android.net.Uri

const val SHOW_ID_ARG = "SHOW_ID"
const val SHOW_TYPE_ARG = "SHOW_TYPE"
const val SHOW_THUMB_IMG_ARG = "SHOW_THUMB_IMG"
const val TWO_PANE_ARG = "TWO_PANE"

class DetailsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: DetailsViewModel

    private var showId: Int? = null
    private var showType: ShowType? = null
    private var showThumbImg: String? = null
    private var twoPane: Boolean = false

    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            showId = it.getInt(SHOW_ID_ARG)
            showType = it.getSerializable(SHOW_TYPE_ARG) as ShowType?
            showThumbImg = it.getString(SHOW_THUMB_IMG_ARG)
            twoPane = it.getBoolean(TWO_PANE_ARG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(DetailsViewModel::class.java)

        setUpToolbar()
        getMovieOrTvShow()
    }

    private fun getMovieOrTvShow() {
        showId?.let { id ->
            showType?.let { type ->
                viewModel.setShowId(id)
                if (type == ShowType.MOVIE) {
                    getMovie()
                } else if (type == ShowType.TV) {
                    getTvShow()
                }
            }
        }
    }

    private fun getMovie() {
        viewModel.getMovie()
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.LOADING -> {
                        updateProgress(true)
                    }
                    Status.SUCCESS -> {
                        updateProgress(false)
                        it.data?.let { movie ->
                            updateUi(
                                movie.getImageUrl(),
                                movie.title,
                                ShowType.MOVIE,
                                movie.video,
                                movie.getDisplayRating(),
                                movie.voteCount,
                                movie.description,
                                movie.genres,
                                movie.date,
                                movie.status
                            )
                            setupFavorites(
                                movie.id,
                                showThumbImg,
                                ShowType.MOVIE,
                                movie.title,
                                movie.getYear()
                            )
                        }
                    }
                    Status.ERROR -> {
                        updateProgress(false)
                        showToast(it.message)
                    }
                }
            })
    }

    private fun getTvShow() {
        viewModel.getTvShow()
            .observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    Status.LOADING -> {
                        updateProgress(true)
                    }
                    Status.SUCCESS -> {
                        updateProgress(false)
                        it.data?.let { tv ->
                            updateUi(
                                tv.getImageUrl(),
                                tv.name,
                                ShowType.TV,
                                tv.video,
                                tv.getDisplayRating(),
                                tv.voteCount,
                                tv.description,
                                tv.genres,
                                tv.getDateDisplay(context),
                                tv.status
                            )
                            setupFavorites(
                                tv.id,
                                showThumbImg,
                                ShowType.TV,
                                tv.name,
                                tv.getYear()
                            )
                        }
                    }
                    Status.ERROR -> {
                        updateProgress(false)
                        showToast(it.message)
                    }
                }
            })
    }

    private fun updateUi(
        imageUrl: String?,
        title: String?,
        type: ShowType,
        video: Video?,
        rating: String?,
        voteCount: Int?,
        description: String?,
        genres: List<Genre>?,
        date: String?,
        status: String?
    ) {
        Glide
            .with(this)
            .load(imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_not_available)
            .into(imageViewMovie)

        textViewMovieTitle.text = title
        textViewMovieType.setText(type.res)
        textViewMovieRating.text = rating
        textViewMovieVoteCount.text = voteCount.toString()
        textViewMovieDescription.text = description
        textViewGenre.text = genres?.joinToString { it.name }
        textViewDate.text = date
        textViewStatus.text = status

        buttonWatchTrailer.isEnabled = !video?.key.isNullOrEmpty()
        buttonWatchTrailer.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(video?.getTrailerUrl())))
        }
    }

    private fun setupFavorites(
        id: Int,
        image: String?,
        type: ShowType,
        title: String?,
        releaseDate: String?
    ) {
        viewModel.isFavorite(id, type).observe(viewLifecycleOwner, Observer {
            if (it == 1) {
                isFavorite = true
                fabFavorites.setImageResource(R.drawable.ic_favorite_white_24dp)
            } else {
                isFavorite = false
                fabFavorites.setImageResource(R.drawable.ic_favorite_border_white_24dp)
            }
        })
        fabFavorites.setOnClickListener {
            if (isFavorite) {
                viewModel.removeFromFavorites(id, type)
                    .observe(viewLifecycleOwner, Observer { removed ->
                        if (removed) {
                            showToast(getString(R.string.removed_from_favorites))
                        }
                    })
            } else {
                viewModel.addToFavorites(id, image, type, title, releaseDate)
                    .observe(viewLifecycleOwner, Observer { added ->
                        if (added) {
                            showToast(getString(R.string.added_to_favorites))
                        }
                    })
            }
        }
    }

    private fun updateProgress(showProgress: Boolean) {
        progressBarFetchDetails.isVisible = showProgress
        detailsLayout.isVisible = !showProgress
        fabFavorites.isVisible = !showProgress
    }

    private fun setUpToolbar() {
        // if two pane do not set back arrow
        if (!twoPane) {
            toolbarDetails.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
            toolbarDetails.setNavigationOnClickListener { activity?.onBackPressed() }
        }
    }

    companion object {

        fun newInstance(
            showId: Int,
            type: ShowType,
            showImg: String?,
            twoPane: Boolean = false
        ): DetailsFragment =
            DetailsFragment().apply {
                arguments = Bundle(4).apply {
                    putInt(SHOW_ID_ARG, showId)
                    putSerializable(SHOW_TYPE_ARG, type)
                    putString(SHOW_THUMB_IMG_ARG, showImg)
                    putBoolean(TWO_PANE_ARG, twoPane)
                }
            }
    }
}
