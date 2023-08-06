package com.example.powerhouseassignment.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.powerhouseassignment.R
import com.example.powerhouseassignment.WeatherList
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

class WeatherToday : RecyclerView.Adapter<TodayHolder>() {

  private var listOfTodayWeather = listOf<WeatherList>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.item_weather_card, parent, false)
    return TodayHolder(view)

  }

  override fun getItemCount(): Int {
    return listOfTodayWeather.size

  }

  @RequiresApi(Build.VERSION_CODES.O)
  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(holder: TodayHolder, position: Int) {
    val todayForeCast = listOfTodayWeather[position]

    if(position > 0){
      if (todayForeCast.wind?.speed?: 0.0 < listOfTodayWeather[position-1]?.wind?.speed?:0.0){
        holder.speedIndicator.rotation = 145F
      }
    }

    holder.timeDisplay.text = todayForeCast.dtTxt!!.substring(11, 16)
    val temperatureFahrenheit = todayForeCast.main?.temp
    val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
    val temperatureFormatted = String.format("%.2f", temperatureCelsius)


    holder.tempDisplay.text = "$temperatureFormatted°"
    holder.speedDisplay.text = "${todayForeCast.wind?.speed} m/s"

    val calendar = Calendar.getInstance()

    // Define the desired format
    val dateFormat = SimpleDateFormat("HH::mm")
    val formattedTime = dateFormat.format(calendar.time)
    val timeofapi = todayForeCast.dtTxt!!.split(" ")
    val partafterspace = timeofapi[0]

    Log.e("time", " formatted time:${formattedTime}, timeofapi: ${partafterspace}")


    for (i in todayForeCast.weather) {
      if (i.icon == "01d") {
        Glide.with(holder.imageDisplay.context)
          .load(R.drawable.oned)
          .into(holder.imageDisplay)

      }

      if (i.icon == "01n") {
        Glide.with(holder.imageDisplay.context)
          .load(R.drawable.onen)
          .into(holder.imageDisplay)

      }

      if (i.icon == "02d") {
        Glide.with(holder.imageDisplay.context)
          .load(R.drawable.twod)
          .into(holder.imageDisplay)

      }


      if (i.icon == "02n") {
        Glide.with(holder.imageDisplay.context)
          .load(R.drawable.twon)
          .into(holder.imageDisplay)

      }


      if (i.icon == "03d" || i.icon == "03n") {
        Glide.with(holder.imageDisplay.context)
          .load(R.drawable.threedn)
          .into(holder.imageDisplay)

      }



      if (i.icon == "10d") {
        Glide.with(holder.imageDisplay.context)
          .load(R.drawable.tend)
          .into(holder.imageDisplay)

      }


      if (i.icon == "10n") {
        Glide.with(holder.imageDisplay.context)
          .load(R.drawable.tenn)
          .into(holder.imageDisplay)

      }


      if (i.icon == "04d" || i.icon == "04n") {
        Glide.with(holder.imageDisplay.context)
          .load(R.drawable.fourdn)
          .into(holder.imageDisplay)

      }


      if (i.icon == "09d" || i.icon == "09n") {
        Glide.with(holder.imageDisplay.context)
          .load(R.drawable.ninedn)
          .into(holder.imageDisplay)

      }



      if (i.icon == "11d" || i.icon == "11n") {
        Glide.with(holder.imageDisplay.context)
          .load(R.drawable.elevend)
          .into(holder.imageDisplay)

      }


      if (i.icon == "13d" || i.icon == "13n") {
        Glide.with(holder.imageDisplay.context)
          .load(R.drawable.thirteend)
          .into(holder.imageDisplay)

      }

      if (i.icon == "50d" || i.icon == "50n") {
        Glide.with(holder.imageDisplay.context)
          .load(R.drawable.fiftydn)
          .into(holder.imageDisplay)

      }

    }

  }

  fun setList(listOfToday: List<WeatherList>) {
    this.listOfTodayWeather = listOfToday

  }

}

class TodayHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

  val imageDisplay: ImageView = itemView.findViewById(R.id.weather_icon)
  val tempDisplay: TextView = itemView.findViewById(R.id.weather_temppp)
  val timeDisplay: TextView = itemView.findViewById(R.id.weather_time)
  val speedDisplay: TextView = itemView.findViewById(R.id.weather_speed)
  val speedIndicator: ImageView = itemView.findViewById(R.id.speed_indicator)

}



