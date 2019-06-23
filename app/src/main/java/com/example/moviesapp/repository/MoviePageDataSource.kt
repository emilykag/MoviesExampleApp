package com.example.moviesapp.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.moviesapp.api.*
import com.example.moviesapp.util.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviePageDataSource(
    private val movieService: MovieService,
    private val query: String
) : PageKeyedDataSource<Int, SearchResult>() {

    private val firstPage = 1

    val status: MutableLiveData<Status> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, SearchResult>) {
        status.postValue(Status.LOADING)
        movieService.searchMovies(Constants.API_KEY, query, firstPage)
            .enqueue(object : Callback<MovieSearchResponse> {

                override fun onResponse(call: Call<MovieSearchResponse>, response: Response<MovieSearchResponse>) {
                    response.body()?.let {
                        callback.onResult(it.results, null, firstPage + 1)
                        status.postValue(Status.SUCCESS)
                    }
                }

                override fun onFailure(call: Call<MovieSearchResponse>, t: Throwable) {
                    status.postValue(Status.ERROR)
                }
            })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, SearchResult>) {
        status.postValue(Status.LOADING)
        movieService.searchMovies(Constants.API_KEY, query, params.key)
            .enqueue(object : Callback<MovieSearchResponse> {

                override fun onResponse(call: Call<MovieSearchResponse>, response: Response<MovieSearchResponse>) {
                    response.body()?.let {

                        val pageKey = if (params.key >= it.totalPages) null else params.key + 1
                        callback.onResult(it.results, pageKey)
                        status.postValue(Status.SUCCESS)
                    }
                }

                override fun onFailure(call: Call<MovieSearchResponse>, t: Throwable) {
                    status.postValue(Status.ERROR)
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, SearchResult>) {
        // ignored, since we only append
    }
}