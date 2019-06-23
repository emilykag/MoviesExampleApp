package com.example.moviesapp.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.moviesapp.api.MovieService
import com.example.moviesapp.api.SearchResult

class MoviePageDataSourceFactory(
    private val movieService: MovieService,
    private val query: String
) : DataSource.Factory<Int, SearchResult>() {

    val liveDataSource = MutableLiveData<MoviePageDataSource>()

    override fun create(): DataSource<Int, SearchResult> {
        val source = MoviePageDataSource(movieService, query)
        liveDataSource.postValue(source)
        return source
    }
}