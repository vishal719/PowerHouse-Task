package com.example.powerhouseassignment.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.powerhouseassignment.ForeCast

@Dao
interface WeatherDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertWeatherList(weatherList: ForeCast)

  @Query("SELECT * FROM forecast_list")
  fun getAllWeather(): List<ForeCast>
}