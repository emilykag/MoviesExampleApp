package com.example.moviesapp.db.typeconverters

import androidx.room.TypeConverter
import com.example.moviesapp.db.entities.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GenreTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToGenreList(str: String?): List<Genre>? {
        return str?.let {
            Gson().fromJson(str, object : TypeToken<List<Genre>>() {}.type)
        }
    }

    @TypeConverter
    @JvmStatic
    fun genreListToString(list: List<Genre>?): String? {
        return Gson().toJson(list)
    }
}
