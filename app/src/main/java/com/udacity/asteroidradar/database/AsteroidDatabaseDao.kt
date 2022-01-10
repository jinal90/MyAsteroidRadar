package com.udacity.asteroidradar.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.model.Asteroid

/**
 * @author Jinal Tandel
 * @since 08/01/2022
 */
@Dao
interface AsteroidDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsteroid(asteroid: Asteroid)

    @Query("SELECT * FROM asteroid_table WHERE close_approach_date >= :date ORDER BY close_approach_date")
    suspend fun get(date: String): List<Asteroid>

    @Query("SELECT * FROM asteroid_table")
    suspend fun getAsteroids(): List<Asteroid>
}