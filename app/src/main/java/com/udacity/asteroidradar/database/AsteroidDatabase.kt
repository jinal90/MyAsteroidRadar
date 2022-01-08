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

    abstract val asteroidDatabaseDao: AsteroidDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: AsteroidDatabase? = null
        fun getInstance(context: Context): AsteroidDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        "asteroidd_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}