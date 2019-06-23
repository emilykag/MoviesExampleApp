package com.example.moviesapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviesapp.api.Resource
import com.example.moviesapp.api.SearchResult
import com.example.moviesapp.api.Status
import com.example.moviesapp.repository.MovieRepository
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    fun search(query: String): LiveData<Status> {
        return movieRepository.searchMovies(query)
    }

    fun getSearchResultLiveData(): LiveData<PagedList<SearchResult>> {
        return movieRepository.searchResultLiveData
    }
}