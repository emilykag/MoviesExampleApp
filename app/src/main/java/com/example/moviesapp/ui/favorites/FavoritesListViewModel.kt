package com.example.moviesapp.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.example.moviesapp.db.entities.Favorite
import com.example.moviesapp.repository.FavoritesRepository
import javax.inject.Inject

class FavoritesListViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    fun loadFavorites(): LiveData<PagedList<Favorite>> {
        return favoritesRepository.loadFavorites()
    }
}