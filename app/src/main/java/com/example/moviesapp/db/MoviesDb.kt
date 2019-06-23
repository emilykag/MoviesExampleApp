package com.example.moviesapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesapp.db.dao.MovieDao
import com.example.moviesapp.db.entities.Movie
import com.example.moviesapp.db.entities.TvShow

@Database(
    entities = [
        Movie::class,
        TvShow::class
    ],
    version = 2,
    exportSchema = false
)
abstract class MoviesDb : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}
