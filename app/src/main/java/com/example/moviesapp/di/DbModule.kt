package com.example.moviesapp.di

import android.app.Application
import androidx.room.Room
import com.example.moviesapp.db.MoviesDb
import com.example.moviesapp.db.dao.FavoritesDao
import com.example.moviesapp.db.dao.MovieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): MoviesDb {
        return Room
            .databaseBuilder(app, MoviesDb::class.java, "Movies.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(moviesDb: MoviesDb): MovieDao {
        return moviesDb.movieDao()
    }

    @Singleton
    @Provides
    fun provideFavoritesDao(moviesDb: MoviesDb): FavoritesDao {
        return moviesDb.favoritesDao()
    }
}