package com.example.moviesapp.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviesapp.db.entities.Movie
import com.example.moviesapp.db.entities.TvShow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Query("SELECT * FROM Movie WHERE id = :id")
    fun loadMovie(id: Int): LiveData<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTvShow(tvShow: TvShow)

    @Query("SELECT * FROM TvShow WHERE id = :id")
    fun loadTvShow(id: Int): LiveData<TvShow>
}