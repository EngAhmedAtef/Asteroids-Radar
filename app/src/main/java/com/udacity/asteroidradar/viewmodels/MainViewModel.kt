package com.udacity.asteroidradar.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.database.getAsteroidsDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.network.ApiObject
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.util.Constants
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {
    enum class AsteroidsShown { TODAY, WEEK, ALL }

    private val _imgOfToday = MutableLiveData<PictureOfDay>()
    val imgOfToday: LiveData<PictureOfDay>
        get() = _imgOfToday

    private val _selectedAsteroid = MutableLiveData<Asteroid>()
    val selectedAsteroid: LiveData<Asteroid>
        get() = _selectedAsteroid

    private val _asteroidsFilter = MutableLiveData<AsteroidsShown>()
    val asteroidsFilter: LiveData<AsteroidsShown>
        get() = _asteroidsFilter

    // Define the database and the repository
    private val database = getAsteroidsDatabase(application)
    private val repository = AsteroidsRepository(database)

    init {
        viewModelScope.launch {
            repository.updateAsteroids()
        }
        _asteroidsFilter.value = AsteroidsShown.ALL
        getImageOfToday()
    }

    var asteroids = repository.savedAsteroids

    private fun getImageOfToday() {
        ApiObject.apiService.getImageOfTheDay(Constants.API_KEY)
            .enqueue(object : Callback<PictureOfDay> {
                override fun onResponse(
                    call: Call<PictureOfDay>,
                    response: Response<PictureOfDay>
                ) {
                    response.body()?.let {
                        _imgOfToday.value = it
                    }
                }

                override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                    Log.e("MainViewModel", "Error downloading Image Of Today: ${t.message}")
                }
            })
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _selectedAsteroid.value = asteroid
    }

    fun onDisplayAsteroidDetails() {
        _selectedAsteroid.value = null
    }

    fun changeShownAsteroids(type: AsteroidsShown) {
        asteroids = when (type) {
            AsteroidsShown.WEEK -> {
                repository.weeksAsteroids
            }
            AsteroidsShown.TODAY -> {
                repository.todaysAsteroids
            }
            AsteroidsShown.ALL -> {
                repository.savedAsteroids
            }
        }
        _asteroidsFilter.value = type
    }
}