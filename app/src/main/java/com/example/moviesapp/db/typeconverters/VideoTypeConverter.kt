package com.example.moviesapp.db.typeconverters

import androidx.room.TypeConverter
import com.example.moviesapp.db.entities.Genre
import com.example.moviesapp.db.entities.Video
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object VideoTypeConverter {

    @TypeConverter
    @JvmStatic
    fun stringToVideo(str: String?): Video? {
        return str?.let {
            Gson().fromJson(str, object : TypeToken<Video>() {}.type)
        }
    }

    @TypeConverter
    @JvmStatic
    fun videoToString(ob: Video?): String? {
        return Gson().toJson(ob)
    }
}