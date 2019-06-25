package com.example.moviesapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.moviesapp.api.Status

/**
 * Data class that is necessary for a UI to show a listing and interact w/ the rest of the system
 */
data class Listing<T>(
    val pagedList: LiveData<PagedList<T>>,
    val status: LiveData<Status>
)