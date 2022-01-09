package com.udacity.asteroidradar.view

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.ImageOfTheDayApi
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabaseDao
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.ImageOfTheDayNasa
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(
    val database: AsteroidDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private val _asteroidList = MutableLiveData<ArrayList<Asteroid>>()
    val asteroidList: LiveData<ArrayList<Asteroid>>
        get() = _asteroidList

    private val _imageOfTheDayUrl = MutableLiveData<String>()
    val imageOfTheDayUrl: LiveData<String>
        get() = _imageOfTheDayUrl

    init {
        initializeAsteroids()
    }

    private fun initializeAsteroids() = viewModelScope.launch(Dispatchers.IO) {
        val asteroids = database.get(getCurrentDate())
        _asteroidList.postValue(ArrayList(asteroids))
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    private fun saveAsteroids() = viewModelScope.launch(Dispatchers.IO) {
        _asteroidList.value.let {
            if (it != null) {
                for (asteroid in it) {
                    database.insertAsteroid(asteroid)
                }
            }
        }
    }

    fun getAsteroids() {
        val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()

        AsteroidApi.retrofitService.getAsteroids(
            nextSevenDaysFormattedDates[0],
            nextSevenDaysFormattedDates[nextSevenDaysFormattedDates.size - 1],
            ""
        ).enqueue(
            object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.d("MainViewModel", "Fail")
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {

                    if (response.isSuccessful) {
                        Log.d("MainViewModel", "Success")
                        _asteroidList.value = parseAsteroidsJsonResult(JSONObject(response.body()))
                        saveAsteroids()
                        Log.d("MainViewModel", "Success ${_asteroidList.value}")
                    } else {
                        Log.d("MainViewModel", "Fail")
                    }
                }
            }
        )
    }

    fun getTheImageOfTheDay() {
        ImageOfTheDayApi.retrofitService.getImageOfTheDay(
            ""
        ).enqueue(
            object : Callback<ImageOfTheDayNasa> {
                override fun onFailure(call: Call<ImageOfTheDayNasa>, t: Throwable) {
                    Log.d("MainViewModel", "Fail image load")
                }

                override fun onResponse(
                    call: Call<ImageOfTheDayNasa>,
                    response: Response<ImageOfTheDayNasa>
                ) {
                    if (response.isSuccessful) {
                        Log.d("MainViewModel", "Success image load")
                        val imageOfTheDayNasa: ImageOfTheDayNasa? = response.body()
                        _imageOfTheDayUrl.value = imageOfTheDayNasa?.hdurl
                    } else {
                        Log.d("MainViewModel", "Fail image load")
                    }
                }
            }
        )
    }
}