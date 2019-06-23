package com.example.moviesapp.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide

import com.example.moviesapp.R
import com.example.moviesapp.api.SearchResult
import com.example.moviesapp.api.Status
import com.example.moviesapp.db.entities.Genre
import com.example.moviesapp.di.Injectable
import com.example.moviesapp.util.extensions.showToast
import kotlinx.android.synthetic.main.fragment_details.*
import javax.inject.Inject

private const val MOVIE_ARG = "MOVIE"

class DetailsFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var detailsViewModel: DetailsViewModel

    private var item: SearchResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            item = it.getParcelable(MOVIE_ARG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        detailsViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(DetailsViewModel::class.java)

        getMovieOrTvShow()
    }

    private fun getMovieOrTvShow() {
        item?.let { item ->
            if (item.type == SearchResult.Type.MOVIE) {
                getMovie(item)
            } else if (item.type == SearchResult.Type.TV) {
                getTvShow(item)
            }
        }
    }

    private fun getMovie(item: SearchResult) {
        detailsViewModel.getMovie(item.id)
            .observe(this, Observer {
                when (it.status) {
                    Status.LOADING -> {

                    }
                    Status.SUCCESS -> {
                        it.data?.let { movie ->
                            updateCommonUi(
                                movie.getImageUrl(),
                                movie.title,
                                SearchResult.Type.MOVIE,
                                movie.video,
                                movie.getDisplayRating(),
                                movie.voteCount,
                                movie.description,
                                movie.genres,
                                movie.date,
                                movie.status
                            )
                        }
                    }
                    Status.ERROR -> {
                        showToast(it.message)
                    }
                }
            })
    }

    private fun getTvShow(item: SearchResult) {
        detailsViewModel.getTvShow(item.id)
            .observe(this, Observer {
                when (it.status) {
                    Status.LOADING -> {

                    }
                    Status.SUCCESS -> {
                        it.data?.let { tv ->
                            updateCommonUi(
                                tv.getImageUrl(),
                                tv.name,
                                SearchResult.Type.TV,
                                false,
                                tv.getDisplayRating(),
                                tv.voteCount,
                                tv.description,
                                tv.genres,
                                tv.getDateDisplay(context),
                                tv.status
                            )
                        }
                    }
                    Status.ERROR -> {
                        showToast(it.message)
                    }
                }
            })
    }

    private fun updateCommonUi(
        imageUrl: String?,
        title: String?,
        type: SearchResult.Type,
        video: Boolean?,
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

        video?.let {
            buttonWatchTrailer.isEnabled = it
        }
    }

    companion object {

        fun newInstance(item: SearchResult): DetailsFragment = DetailsFragment().apply {
            arguments = Bundle(1).apply {
                putParcelable(MOVIE_ARG, item)
            }
        }
    }
}
