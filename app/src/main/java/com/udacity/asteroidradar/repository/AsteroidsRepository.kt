package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainAsteroid
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.ApiObject
import com.udacity.asteroidradar.network.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.network.getTodayAsString
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    val savedAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getSavedAsteroids()) {
            it.asDomainAsteroid()
        }

    val todaysAsteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao.getTodaysAsteroids(
            getTodayAsString()
        )
    ) {
        it.asDomainAsteroid()
    }

    val weeksAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(
            database.asteroidDao.getWeekAsteroids(
                getTodayAsDate().time,
                getDateAfterWeek().time - 1
            )
        ) {
            it.asDomainAsteroid()
        }

    // Downloads the Asteroids from the API and stores them into the database.
    suspend fun updateAsteroids() {
        Log.i("AsteroidsRepository", "Called updateAsteroids()")
        withContext(Dispatchers.IO) {
            try {
                val weekDates = getNextSevenDaysFormattedDates()
                val asteroidsString =
                    ApiObject.apiService.getDataFromApi(Constants.API_KEY, weekDates.first(), weekDates.last())
                val jsonObject = JSONObject(asteroidsString)
                val asteroidsList = parseAsteroidsJsonResult(jsonObject)
                Log.i("AsteroidsRepository", "Asteroids list size is ${asteroidsList.size}")
                database.asteroidDao.insertAll(asteroidsList.asDatabaseAsteroid())
            } catch (e: Exception) {
                Log.e("AsteroidsRepository", "Error calling updateAsteroids(): ${e.message}")
                e.printStackTrace()
            }
        }
    }
}