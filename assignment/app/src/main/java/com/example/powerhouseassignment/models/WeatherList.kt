package com.example.powerhouseassignment

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class WeatherList(
  @SerializedName("main") var main: Main? = Main(),
  @SerializedName("weather") var weather: ArrayList<Weather> = arrayListOf(),
  @SerializedName("clouds") var clouds: Clouds? = Clouds(),
  @SerializedName("wind") var wind: Wind? = Wind(),
  @SerializedName("visibility") var visibility: Int? = null,
  @SerializedName("pop") var pop: Double? = null,
  @SerializedName("sys") var sys: Sys? = Sys(),
  @SerializedName("dt_txt") var dtTxt: String? = null
)