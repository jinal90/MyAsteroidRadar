package com.udacity.asteroidradar.repository

import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.ImageOfTheDayApi
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabaseDao
import com.udacity.asteroidradar.model.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * @author Jinal Tandel
 * @since 10/01/2022
 */
class MainRepository(private val database: AsteroidDatabaseDao) {

    val asteroidList = MutableLiveData<ArrayList<Asteroid>>()
    val imageOfTheDayUrl = MutableLiveData<String>()

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()

            val asteroidJsonString = AsteroidApi.retrofitService.getAsteroidsAsync(
                nextSevenDaysFormattedDates[0],
                nextSevenDaysFormattedDates[nextSevenDaysFormattedDates.size - 1],
                ""
            ).await()

            val listOfAsteroids = parseAsteroidsJsonResult(JSONObject(asteroidJsonString))
            asteroidList.postValue(listOfAsteroids)
            listOfAsteroids.let {
                for (asteroid in it) {
                    database.insertAsteroid(asteroid)
                }
            }
        }
    }

    suspend fun getAsteroidsFromDatabase(todaysDate: String) {
        withContext(Dispatchers.IO) {
            val asteroids = database.get(todaysDate)
            asteroidList.postValue(ArrayList(asteroids))
        }
    }

    suspend fun getTheImageOfTheDay() {
        withContext(Dispatchers.IO) {
            val imageOfTheDayNasa = ImageOfTheDayApi.retrofitService.getImageOfTheDayAsync(
                ""
            ).await()
            imageOfTheDayUrl.postValue(imageOfTheDayNasa.hdurl)
        }
    }
}