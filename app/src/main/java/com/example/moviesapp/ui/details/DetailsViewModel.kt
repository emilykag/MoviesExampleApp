package com.example.moviesapp.ui.details

import androidx.lifecycle.LiveData
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

    fun getMovie(id: Int): LiveData<Resource<Movie>> {
        return movieRepository.getMovieById(id)
    }

    fun getTvShow(id: Int): LiveData<Resource<TvShow>> {
        return movieRepository.getTvShowById(id)
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