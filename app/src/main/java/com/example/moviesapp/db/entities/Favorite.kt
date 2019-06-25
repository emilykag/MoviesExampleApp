package com.example.moviesapp.db.entities

import androidx.room.Entity
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.moviesapp.db.typeconverters.DateTypeConverter
import com.example.moviesapp.db.typeconverters.ShowTypeConverter
import com.example.moviesapp.util.Constants
import com.example.moviesapp.util.DateUtils
import java.util.*

@Entity(primaryKeys = ["movieId", "type"])
@TypeConverters(ShowTypeConverter::class, DateTypeConverter::class)
data class Favorite(
    val movieId: Int,
    val image: String?,
    val type: ShowType,
    val title: String?,
    val releaseDate: String?,
    val dateInserted: Date?
) {
    fun getFavImageUrl(): String {
        return Constants.IMAGE_URL + Constants.FAV_IMAGE_SIZE + image
    }
}