package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.MainRepository
import retrofit2.HttpException

/**
 * @author Jinal Tandel
 * @since 10/01/2022
 */
class RefreshAsteroidDataWork(private val appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshAsteroidDataWork"
    }

    override suspend fun doWork(): Result {

        val dataSource = AsteroidDatabase.getInstance(appContext).asteroidDatabaseDao()
        val asteroidRepository = MainRepository(dataSource)

        return try {
            asteroidRepository.refreshAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}