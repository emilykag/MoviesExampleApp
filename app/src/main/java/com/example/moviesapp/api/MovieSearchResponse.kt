package com.example.moviesapp.api

import com.google.gson.annotations.SerializedName

data class MovieSearchResponse(
    val page: Int = 0,
    @SerializedName("total_results")
    val totalResults: Int = 0,
    @SerializedName("total_pages")
    val totalPages: Int = 0,
    val results: List<SearchResult>
)