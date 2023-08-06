package com.example.powerhouseassignment.db

import androidx.room.TypeConverter
import com.example.powerhouseassignment.City
import com.example.powerhouseassignment.Clouds
import com.example.powerhouseassignment.Main
import com.example.powerhouseassignment.Sys
import com.example.powerhouseassignment.Weather
import com.example.powerhouseassignment.WeatherList
import com.example.powerhouseassignment.Wind
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromMain(main: Main?): String? = Gson().toJson(main)

    @TypeConverter
    fun toMain(mainJson: String?): Main? =
        Gson().fromJson(mainJson, Main::class.java)

    @TypeConverter
    fun fromWeather(weatherList: ArrayList<Weather>?): String? {
        return Gson().toJson(weatherList)
    }

    @TypeConverter
    fun toWeather(weatherListJson: String?): ArrayList<Weather>? {
        val listType = object : TypeToken<ArrayList<Weather>>() {}.type
        return Gson().fromJson(weatherListJson, listType)
    }

    @TypeConverter
    fun fromWeatherList(weatherList: ArrayList<WeatherList>): String {
        return Gson().toJson(weatherList)
    }

    @TypeConverter
    fun toWeatherList(json: String): ArrayList<WeatherList> {
        val listType = object : TypeToken<ArrayList<WeatherList>>() {}.type
        return Gson().fromJson(json, listType)
    }
    @TypeConverter
    fun fromClouds(clouds: Clouds?): String? = Gson().toJson(clouds)

    @TypeConverter
    fun toClouds(cloudsJson: String?): Clouds? =
        Gson().fromJson(cloudsJson, Clouds::class.java)

    @TypeConverter
    fun fromWind(wind: Wind?): String? = Gson().toJson(wind)

    @TypeConverter
    fun toWind(windJson: String?): Wind? =
        Gson().fromJson(windJson, Wind::class.java)

    @TypeConverter
    fun fromSys(sys: Sys?): String? = Gson().toJson(sys)

    @TypeConverter
    fun toSys(sysJson: String?): Sys? =
        Gson().fromJson(sysJson, Sys::class.java)

    @TypeConverter
    fun fromCity(city: City): String {
        return Gson().toJson(city)
    }

    @TypeConverter
    fun toCity(json: String): City {
        return Gson().fromJson(json, City::class.java)
    }
}
