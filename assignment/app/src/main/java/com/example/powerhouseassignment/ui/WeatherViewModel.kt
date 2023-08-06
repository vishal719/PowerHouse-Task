package com.example.powerhouseassignment.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.powerhouseassignment.ForeCast
import com.example.powerhouseassignment.MyApplication
import com.example.powerhouseassignment.SharedPrefs
import com.example.powerhouseassignment.WeatherList
import com.example.powerhouseassignment.api.RetrofitInstance
import com.example.powerhouseassignment.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class WeatherViewModel(app: Application, val weatherRepository: WeatherRepository) :
  AndroidViewModel(app) {

  val todayWeatherLiveData = MutableLiveData<List<WeatherList>>()
  val weatherLiveData = MutableLiveData<WeatherList?>()
  val cityName = MutableLiveData<String>()
  val showToast = MutableLiveData<Boolean>()

  init {
    getCachedWeather()

  }

  @RequiresApi(Build.VERSION_CODES.O)
  fun getWeather(city: String? = null, lat: Double? = null, lon: Double? = null) =
    viewModelScope.launch(Dispatchers.IO) {
      val todayWeatherList = mutableListOf<WeatherList>()
      val currentDateTime = LocalDateTime.now()
      val currentDateO = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

      if (hasInternetConnection()){

        val call = if (city != null) {
          weatherRepository.getWeather(city)
        } else {
          weatherRepository.getLatLonWeather(lat.toString(), lon.toString())
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
          cacheWeather(response.body()!!)
        } else {
          val errorMessage = response.message()
          Log.e("CurrentWeatherError", "Error: $errorMessage")
        }
      }
      else{
        showToast.postValue(true)
      }
    }

  fun getCachedWeather() = viewModelScope.launch(Dispatchers.IO) {
    val forecast: List<ForeCast> = weatherRepository.getCachedWeather()
    if (forecast.isNotEmpty()){

      val weatherList = forecast[forecast.size-1].weatherList
      val todayWeatherList = mutableListOf<WeatherList>()
      val currentDateTime = LocalDateTime.now()
      val currentDateO = currentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
      cityName.postValue(forecast[forecast.size-1].city!!.name)
      val currentDate = currentDateO

      weatherList?.forEach { weather ->
        if (weather.dtTxt!!.split("\\s".toRegex()).contains(currentDate)) {
          todayWeatherList.add(weather)
        }
      }
      val closestWeather = findClosestWeather(todayWeatherList)
      weatherLiveData.postValue(closestWeather)

      todayWeatherLiveData.postValue(todayWeatherList)
    }
  }

  fun cacheWeather(list: ForeCast) = viewModelScope.launch {
    weatherRepository.insertWeather(list)
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

  private fun hasInternetConnection(): Boolean {
    val connectivityManager = getApplication<MyApplication>().getSystemService(
      Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      val activeNetwork = connectivityManager.activeNetwork ?: return false
      val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
      return when {
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
      }
    } else {
      connectivityManager.activeNetworkInfo?.run {
        return when (type) {
          ConnectivityManager.TYPE_WIFI -> true
          ConnectivityManager.TYPE_MOBILE -> true
          ConnectivityManager.TYPE_ETHERNET -> true
          else -> false
        }
      }
    }
    return false
  }

  private fun timeToMinutes(time: String): Int {
    val parts = time.split(":")
    return parts[0].toInt() * 60 + parts[1].toInt()
  }

}