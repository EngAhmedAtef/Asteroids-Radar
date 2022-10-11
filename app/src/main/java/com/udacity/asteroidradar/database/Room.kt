package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM asteroidstable ORDER BY epochCloseApproachDate ASC")
    fun getSavedAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroidstable WHERE closeApproachDate = :todaysDate ORDER BY epochCloseApproachDate ASC")
    fun getTodaysAsteroids(todaysDate: String): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroidstable WHERE epochCloseApproachDate BETWEEN :startEpochCloseApproachDate AND :endEpochCloseApproachDate ORDER BY closeApproachDate ASC")
    fun getWeekAsteroids(startEpochCloseApproachDate: Long, endEpochCloseApproachDate: Long): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: List<DatabaseAsteroid>)
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getAsteroidsDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if(!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context,
                AsteroidsDatabase::class.java,
                "Asteroids_Database"
            ).build()
        }
    }
    return INSTANCE
}