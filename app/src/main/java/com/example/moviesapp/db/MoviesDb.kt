package com.example.moviesapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesapp.db.dao.FavoritesDao
import com.example.moviesapp.db.dao.MovieDao
import com.example.moviesapp.db.entities.Favorite
import com.example.moviesapp.db.entities.Movie
import com.example.moviesapp.db.entities.TvShow

@Database(
    entities = [
        Movie::class,
        TvShow::class,
        Favorite::class
    ],
    version = 3,
    exportSchema = false
)
abstract class MoviesDb : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun favoritesDao(): FavoritesDao
}
