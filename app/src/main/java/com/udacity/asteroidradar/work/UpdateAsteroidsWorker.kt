package com.udacity.asteroidradar.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getAsteroidsDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import java.lang.Exception

class UpdateAsteroidsWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "UpdateAsteroidsWorker"
    }

    override suspend fun doWork(): Result {
        val database = getAsteroidsDatabase(applicationContext)
        val repository =  AsteroidsRepository(database)

        return try {
            repository.updateAsteroids()
            Log.i("UpdateAsteroidsWorker", "Successfully called updateAsteroids()")
            Result.success()
        } catch (e: Exception) {
            Log.i("UpdateAsteroidsWorker", "A problem calling updateAsteroids(), retrying..")
            Result.retry()
        }
    }

}