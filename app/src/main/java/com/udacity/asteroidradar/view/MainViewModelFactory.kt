package com.udacity.asteroidradar.view

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.database.AsteroidDatabaseDao

/**
 * @author Jinal Tandel
 * @since 08/01/2022
 */
class MainViewModelFactory(
    private val database: AsteroidDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(database, application ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}