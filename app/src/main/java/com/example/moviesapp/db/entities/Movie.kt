package com.example.moviesapp.db.entities

import androidx.room.Entity
import androidx.room.TypeConverters
import com.example.moviesapp.api.adapter.VideosJsonAdapter
import com.example.moviesapp.db.typeconverters.GenreTypeConverter
import com.example.moviesapp.db.typeconverters.VideoTypeConverter
import com.example.moviesapp.util.Constants
import com.example.moviesapp.util.DateUtils
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
@TypeConverters(GenreTypeConverter::class, VideoTypeConverter::class)
data class Movie(
    val id: Int,
    @SerializedName("backdrop_path")
    val image: String?,
    val title: String?,
    @SerializedName("release_date")
    val date: String?,
    @SerializedName("vote_average")
    val rating: Double?,
    val genres: List<Genre>?,
    @SerializedName("overview")
    val description: String?,
    val status: String?,
    @SerializedName("vote_count")
    val voteCount: Int?,
    @SerializedName("videos")
    val video: Video?
) {
    fun getImageUrl(): String {
        return Constants.IMAGE_URL + Constants.MAIN_IMAGE_SIZE + image
    }

    fun getDisplayRating(): String {
        return if (rating == 0.0) {
            "-"
        } else {
            String.format("%.1f", rating)
        }
    }

    fun getYear(): String {
        return DateUtils.getYearFromDate(date)
    }
}