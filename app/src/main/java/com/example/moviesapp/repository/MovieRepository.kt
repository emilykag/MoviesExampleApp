package com.example.moviesapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.moviesapp.api.*
import com.example.moviesapp.db.dao.MovieDao
import com.example.moviesapp.db.entities.Movie
import com.example.moviesapp.db.entities.TvShow
import com.example.moviesapp.util.AppExecutors
import com.example.moviesapp.util.Constants
import com.example.moviesapp.util.Constants.Companion.DEFAULT_PAGE_SIZE
import com.example.moviesapp.util.NetworkBoundResource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val movieService: MovieService,
    private val movieDao: MovieDao
) {
    var searchResultLiveData: LiveData<PagedList<SearchResult>> = MutableLiveData()

    fun searchMovies(query: String): LiveData<Status> {
        val sourceFactory = MoviePageDataSourceFactory(movieService, query)

        val livePagedList = sourceFactory.toLiveData(DEFAULT_PAGE_SIZE, null)
        val refreshState = Transformations.switchMap(sourceFactory.liveDataSource) {
            it.status
        }

        searchResultLiveData = livePagedList

        return refreshState
    }

    fun getMovieById(id: Int): LiveData<Resource<Movie>> {
        return object : NetworkBoundResource<Movie, Movie>(appExecutors) {

            override fun saveCallResult(item: Movie) = movieDao.insertMovie(item)

            override fun shouldFetch(data: Movie?): Boolean = data == null

            override fun loadFromDb(): LiveData<Movie> = movieDao.loadMovie(id)

            override fun createCall(): LiveData<ApiResponse<Movie>> =
                movieService.fetchMovieDetails(id, Constants.API_KEY)

        }.asLiveData()
    }

    fun getTvShowById(id: Int): LiveData<Resource<TvShow>> {
        return object : NetworkBoundResource<TvShow, TvShow>(appExecutors) {

            override fun saveCallResult(item: TvShow) = movieDao.insertTvShow(item)

            override fun shouldFetch(data: TvShow?): Boolean = data == null

            override fun loadFromDb(): LiveData<TvShow> = movieDao.loadTvShow(id)

            override fun createCall(): LiveData<ApiResponse<TvShow>> =
                movieService.fetchTvShowDetails(id, Constants.API_KEY)

        }.asLiveData()
    }
}