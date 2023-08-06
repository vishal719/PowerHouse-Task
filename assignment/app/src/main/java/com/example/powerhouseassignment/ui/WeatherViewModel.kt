package com.example.powerhouseassignment.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.powerhouseassignment.MyApplication
import com.example.powerhouseassignment.SharedPrefs
import com.example.powerhouseassignment.WeatherList
import com.example.powerhouseassignment.api.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WeatherViewModel : ViewModel() {

  val todayWeatherLiveData = MutableLiveData<List<WeatherList>>()
  val forecastLiveData = MutableLiveData<List<WeatherList>>()
  val weatherLiveData = MutableLiveData<WeatherList?>()
  val cityName = MutableLiveData<String>()
  val context = MyApplication.instance

  @RequiresApi(Build.VERSION_CODES.O)
  fun getWeather(city: String? = null, lat: Double? = null, lon: Double? = null) = viewModelScope.launch(Dispatchers.IO) {
    val todayWeatherList = mutableListOf<WeatherList>()
    val currentDateTime = LocalDateTime.now()
    val currentDateO = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    val call = if (city != null) {
      RetrofitInstance.api.getWeatherByCity(city)
    } else {
      RetrofitInstance.api.getCurrentWeather(lat.toString(), lon.toString())
    }
    val response = call.execute()

    if (response.isSuccessful) {
      val weatherList = response.body()?.weatherList

      cityName.postValue(response.body()?.city!!.name)
      val currentDate = currentDateO

      weatherList?.forEach { weather ->
        if (weather.dtTxt!!.split("\\s".toRegex()).contains(currentDate)) {
          todayWeatherList.add(weather)
        }
      }
      val closestWeather = findClosestWeather(todayWeatherList)
      weatherLiveData.postValue(closestWeather)

      todayWeatherLiveData.postValue(todayWeatherList)

    } else {
      val errorMessage = response.message()
      Log.e("CurrentWeatherError", "Error: $errorMessage")
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun getForecastUpcoming(city: String? = null) = viewModelScope.launch(Dispatchers.IO) {
    val forecastWeatherList = mutableListOf<WeatherList>()
    val currentDateTime = LocalDateTime.now()
    val currentDateO = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    val sharedPrefs = SharedPrefs.getInstance(context)
    val lat = sharedPrefs.getValue("lat").toString()
    val lon = sharedPrefs.getValue("lon").toString()
    val call = if (city != null) {
      RetrofitInstance.api.getWeatherByCity(city)
    } else {
      RetrofitInstance.api.getCurrentWeather(lat, lon)
    }
    val response = call.execute()

    if (response.isSuccessful) {
      val weatherList = response.body()?.weatherList
      val currentDate = currentDateO

      weatherList?.forEach { weather ->
        if (!weather.dtTxt!!.split("\\s".toRegex()).contains(currentDate)) {
          if (weather.dtTxt!!.substring(11, 16) == "12:00") {
            forecastWeatherList.add(weather)

          }
        }
      }

      forecastLiveData.postValue(forecastWeatherList)



      Log.d("Forecast LiveData", forecastLiveData.value.toString())
    } else {
      val errorMessage = response.message()
      Log.e("CurrentWeatherError", "Error: $errorMessage")
    }
  }

  @RequiresApi(Build.VERSION_CODES.O)
  private fun findClosestWeather(weatherList: List<WeatherList>): WeatherList? {
    val systemTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
    var closestWeather: WeatherList? = null
    var minTimeDifference = Int.MAX_VALUE

    for (weather in weatherList) {
      val weatherTime = weather.dtTxt!!.substring(11, 16)
      val timeDifference = Math.abs(timeToMinutes(weatherTime) - timeToMinutes(systemTime))

      if (timeDifference < minTimeDifference) {
        minTimeDifference = timeDifference
        closestWeather = weather
      }
    }

    return closestWeather
  }

  private fun timeToMinutes(time: String): Int {
    val parts = time.split(":")
    return parts[0].toInt() * 60 + parts[1].toInt()
  }

}