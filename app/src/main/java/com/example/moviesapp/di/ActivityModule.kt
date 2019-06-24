package com.example.moviesapp.di

import com.example.moviesapp.ui.favorites.FavoritesActivity
import com.example.moviesapp.ui.search.SearchActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeSearchActivity(): SearchActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeFavoritesActivity(): FavoritesActivity
}
