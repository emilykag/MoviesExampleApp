package com.example.moviesapp.db.typeconverters

import androidx.room.TypeConverter
import com.example.moviesapp.db.entities.ShowType

object ShowTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToShowType(str: String?): ShowType? {
        return str?.let {
            when (it) {
                "MOVIE" -> ShowType.MOVIE
                "TV" -> ShowType.TV
                "PERSON" -> ShowType.PERSON
                else -> null
            }
        }
    }

    @TypeConverter
    @JvmStatic
    fun showTypeToString(type: ShowType): String? {
        return type.name
    }
}