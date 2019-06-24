package com.example.moviesapp.db.entities

import com.example.moviesapp.R
import com.google.gson.annotations.SerializedName

enum class ShowType(val res: Int) {

    @SerializedName("movie")
    MOVIE(R.string.movie),
    @SerializedName("tv")
    TV(R.string.tv_series),
    @SerializedName("person")
    PERSON(R.string.person)
}