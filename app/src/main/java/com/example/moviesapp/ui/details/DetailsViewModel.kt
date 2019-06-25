package com.example.moviesapp.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.moviesapp.api.Resource
import com.example.moviesapp.api.SearchResult
import com.example.moviesapp.db.entities.Movie
import com.example.moviesapp.db.entities.ShowType
import com.example.moviesapp.db.entities.TvShow
import com.example.moviesapp.repository.FavoritesRepository
import com.example.moviesapp.repository.MovieRepository
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _showId = MutableLiveData<Int>()

    private val movie: LiveData<Resource<Movie>> = Transformations.switchMap(_showId) {
        movieRepository.getMovieById(it)
    }
    private val tvShow: LiveData<Resource<TvShow>> = Transformations.switchMap(_showId) {
        movieRepository.getTvShowById(it)
    }

    fun getMovie(): LiveData<Resource<Movie>> {
        return movie
    }

    fun getTvShow(): LiveData<Resource<TvShow>> {
        return tvShow
    }

    fun setShowId(id: Int) {
        if (_showId.value == id) {
            return
        }
        _showId.value = id
    }

    fun isFavorite(id: Int, type: ShowType): LiveData<Int> {
        return favoritesRepository.isFavorite(id, type)
    }

    fun addToFavorites(
        movieId: Int,
        image: String?,
        type: ShowType,
        title: String?,
        releaseDate: String?
    ): LiveData<Boolean> {
        return favoritesRepository.addToFavorites(movieId, image, type, title, releaseDate)
    }

    fun removeFromFavorites(movieId: Int, type: ShowType): LiveData<Boolean> {
        return favoritesRepository.removeFromFavorites(movieId, type)
    }
}