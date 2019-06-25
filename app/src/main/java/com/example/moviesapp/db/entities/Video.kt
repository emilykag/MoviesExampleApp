package com.example.moviesapp.db.entities

import com.example.moviesapp.util.Constants

data class Video(val key: String?) {

    fun getTrailerUrl(): String? {
        return Constants.YOU_TUBE_URL + key
    }
}