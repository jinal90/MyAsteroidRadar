package com.udacity.asteroidradar.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.checkConnection
import com.udacity.asteroidradar.database.AsteroidDatabaseDao
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(
    database: AsteroidDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val repository = MainRepository(database)

    val asteroidList: LiveData<ArrayList<Asteroid>>
        get() = repository.asteroidList

    val imageOfTheDayUrl: LiveData<String>
        get() = repository.imageOfTheDayUrl

    init {
        initializeAsteroids()
        if (checkConnection(application)) {
            getAsteroids()
            getTheImageOfTheDay()
        }
    }

    private fun initializeAsteroids() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAsteroidsFromDatabase(getCurrentDate())
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    fun getAsteroids() = viewModelScope.launch {
        repository.refreshAsteroids()
    }

    fun getTheImageOfTheDay() = viewModelScope.launch {
        repository.getTheImageOfTheDay()
    }
}