package com.example.moviesapp.api

import androidx.lifecycle.LiveData
import com.example.moviesapp.db.entities.Movie
import com.example.moviesapp.db.entities.TvShow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("3/search/multi")
    fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): Call<MovieSearchResponse>

    @GET("3/movie/{id}")
    fun fetchMovieDetails(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): LiveData<ApiResponse<Movie>>

    @GET("3/tv/{id}")
    fun fetchTvShowDetails(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String
    ): LiveData<ApiResponse<TvShow>>
}