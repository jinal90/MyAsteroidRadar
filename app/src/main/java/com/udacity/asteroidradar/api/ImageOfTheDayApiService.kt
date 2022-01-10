package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.model.ImageOfTheDayNasa
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * @author Jinal Tandel
 * @since 28/12/2021
 */

private val client: OkHttpClient? = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL)
    .client(client)
    .build()

interface ImageOfTheDayApiService {
    @GET("planetary/apod")
    fun getImageOfTheDayAsync(@Query("api_key") api_key: String):
            Deferred<ImageOfTheDayNasa>
}

object ImageOfTheDayApi {
    val retrofitService: ImageOfTheDayApiService by lazy {
        retrofit.create(ImageOfTheDayApiService::class.java)
    }
}