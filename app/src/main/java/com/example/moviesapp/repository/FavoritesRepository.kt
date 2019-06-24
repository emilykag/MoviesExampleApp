package com.example.moviesapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.toLiveData
import com.example.moviesapp.db.dao.FavoritesDao
import com.example.moviesapp.db.entities.Favorite
import com.example.moviesapp.db.entities.ShowType
import com.example.moviesapp.util.AppExecutors
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import androidx.paging.Config
import androidx.paging.PagedList
import com.example.moviesapp.util.Constants.Companion.DEFAULT_PAGE_SIZE

class FavoritesRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val favoritesDao: FavoritesDao
) {

    fun addToFavorites(
        movieId: Int,
        image: String?,
        type: ShowType,
        title: String?,
        releaseDate: String?
    ): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()

        try {
            appExecutors.diskIO().execute {
                favoritesDao.insertFavorite(Favorite(movieId, image, type, title, releaseDate, Date()))
                liveData.postValue(true)
            }
        } catch (ex: Exception) {
            liveData.postValue(false)
        }
        return liveData
    }

    fun loadFavorites(): LiveData<PagedList<Favorite>> {
        return favoritesDao.loadFavorites().toLiveData(DEFAULT_PAGE_SIZE, null)
    }

    fun isFavorite(movieId: Int, type: ShowType): LiveData<Int> {
        return favoritesDao.isFavorite(movieId, type.name)
    }

    fun removeFromFavorites(movieId: Int, type: ShowType): LiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()

        try {
            appExecutors.diskIO().execute {
                favoritesDao.deleteFavorite(movieId, type.name)
                liveData.postValue(true)
            }
        } catch (ex: Exception) {
            liveData.postValue(false)
        }
        return liveData
    }

}