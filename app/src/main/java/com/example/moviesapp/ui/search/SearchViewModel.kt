package com.example.moviesapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviesapp.api.SearchResult
import com.example.moviesapp.api.Status
import com.example.moviesapp.repository.MovieRepository
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _query = MutableLiveData<String>()

    private val searchResults = Transformations.map(_query) {
        movieRepository.searchMovies(it)
    }
    private val results = Transformations.switchMap(searchResults) {
        it.pagedList
    }
    private val status = Transformations.switchMap(searchResults) {
        it.status
    }

    fun search(query: String) {
        if (_query.value == query) {
            return
        }
        _query.value = query
    }

    fun getStatus(): LiveData<Status> {
        return status
    }

    fun getSearchResultLiveData(): LiveData<PagedList<SearchResult>> {
        return results
    }

    fun getSearchQuery(): String? {
        return _query.value
    }
}