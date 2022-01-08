package com.udacity.asteroidradar.view

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.ImageOfTheDayApi
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.ImageOfTheDayNasa
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _asteroidList = MutableLiveData<ArrayList<Asteroid>>()
    val asteroidList: LiveData<ArrayList<Asteroid>>
        get() = _asteroidList

    private val _imageOfTheDayUrl = MutableLiveData<String>()
    val imageOfTheDayUrl: LiveData<String>
        get() = _imageOfTheDayUrl

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