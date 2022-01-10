package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.model.Asteroid

/**
 * @author Jinal Tandel
 * @since 08/01/2022
 */

@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {

    abstract fun asteroidDatabaseDao(): AsteroidDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: AsteroidDatabase? = null
        fun getInstance(context: Context): AsteroidDatabase {
            val instance = INSTANCE
            if (instance != null) {
                return instance
            }
            synchronized(this) {
                val tempInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidDatabase::class.java,
                    "asteroid_database"
                ).build()

                INSTANCE = tempInstance

                return tempInstance
            }
        }
    }
}