package com.example.moviesapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviesapp.api.Resource
import com.example.moviesapp.api.SearchResult
import com.example.moviesapp.db.entities.Movie
import com.example.moviesapp.db.entities.TvShow
import com.example.moviesapp.repository.MovieRepository
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    fun getMovie(id: Int): LiveData<Resource<Movie>> {
        return movieRepository.getMovieById(id)
    }

    fun getTvShow(id: Int): LiveData<Resource<TvShow>> {
        return movieRepository.getTvShowById(id)
    }
}