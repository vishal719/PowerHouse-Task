package com.example.powerhouseassignment.repository

import com.example.powerhouseassignment.ForeCast
import com.example.powerhouseassignment.WeatherList
import com.example.powerhouseassignment.api.RetrofitInstance
import com.example.powerhouseassignment.db.WeatherDatabase

class WeatherRepository(val db: WeatherDatabase) {

  suspend fun getWeather(city: String) = RetrofitInstance.api.getWeatherByCity(city)

  suspend fun getLatLonWeather(lat: String? = null, lon: String? = null) = RetrofitInstance.api.getCurrentWeather(lat.toString(), lon.toString())

  suspend fun insertWeather(foreCast: ForeCast) = db.weatherListDao().insertWeatherList(foreCast)
  fun getCachedWeather() = db.weatherListDao().getAllWeather()

}