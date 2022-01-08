package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.udacity.asteroidradar.model.Asteroid

/**
 * @author Jinal Tandel
 * @since 08/01/2022
 */
@Dao
interface AsteroidDatabaseDao {
    @Insert
    suspend fun insertAsteroid(asteroid: Asteroid)

    @Query("SELECT * from asteroid_table WHERE close_approach_date = :date")
    suspend fun get(date: String): Asteroid?

    @Query("SELECT * FROM asteroid_table ORDER BY close_approach_date DESC")
    fun getAsteroids(): LiveData<List<Asteroid>>
}