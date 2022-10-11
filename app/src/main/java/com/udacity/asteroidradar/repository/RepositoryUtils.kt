package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.util.Constants
import java.util.*
import kotlin.collections.ArrayList

fun ArrayList<Asteroid>.asDatabaseAsteroid(): List<DatabaseAsteroid> {
    return map {
        DatabaseAsteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            epochCloseApproachDate = it.epochCloseApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun getTodayAsDate(): Date {
    return Calendar.getInstance().apply {
        timeZone = TimeZone.getTimeZone("UTC")
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time
}

fun getDateAfterWeek(): Date {
    return Calendar.getInstance().apply {
        time = getTodayAsDate()
        add(Calendar.DAY_OF_YEAR, Constants.DEFAULT_END_DATE_DAYS)
    }.time
}