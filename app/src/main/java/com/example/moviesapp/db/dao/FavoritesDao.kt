package com.example.moviesapp.db.dao

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviesapp.db.entities.Favorite
import androidx.paging.DataSource

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @WorkerThread
    fun insertFavorite(favorite: Favorite)

    @Query("SELECT * FROM Favorite ORDER BY dateInserted DESC")
    fun loadFavorites(): DataSource.Factory<Int, Favorite>

    @Query("SELECT EXISTS (SELECT 1 FROM Favorite WHERE movieId=:movieId AND type=:type)")
    fun isFavorite(movieId: Int, type: String): LiveData<Int>

    @Query("DELETE FROM Favorite WHERE movieId=:movieId AND type=:type")
    @WorkerThread
    fun deleteFavorite(movieId: Int, type: String)
}