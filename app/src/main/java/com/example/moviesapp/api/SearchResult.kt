package com.example.moviesapp.api

import android.os.Parcelable
import com.example.moviesapp.R
import com.example.moviesapp.util.Constants
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@Parcelize
data class SearchResult(
    val id: Int,
    @SerializedName("poster_path")
    val image: String?,
    @SerializedName("media_type")
    val type: Type,
    @SerializedName(value = "title", alternate = ["name"])
    val title: String,
    @SerializedName(value = "release_date", alternate = ["first_air_date"])
    val date: String?,
    @SerializedName("vote_average")
    val rating: Double = 0.0
) : Parcelable {

    fun getThumbnailUrl(): String {
        return Constants.IMAGE_URL + Constants.THUMB_SIZE + image
    }

    fun getYear(): String {
        return if (date.isNullOrEmpty()) {
            "-"
        } else {
            try {
                val apiDateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val apiDate = apiDateFormatter.parse(date)
                val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())
                yearFormatter.format(apiDate)
            } catch (ex: ParseException) {
                Timber.d(ex, "Cannot parse $date")
                "-"
            }
        }
    }

    fun getDisplayRating(): String {
        return if (rating == 0.0) {
            "-"
        } else {
            String.format("%.1f", rating)
        }
    }

    enum class Type(val res: Int) {

        @SerializedName("movie")
        MOVIE(R.string.movie),
        @SerializedName("tv")
        TV(R.string.tv_series),
        @SerializedName("person")
        PERSON(R.string.person)
    }
}