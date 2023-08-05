package com.example.powerhouseassignment

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.powerhouseassignment.adapter.WeatherToday
import com.example.powerhouseassignment.databinding.ActivityMainBinding
import com.example.powerhouseassignment.ui.WeatherViewModel
import java.net.URI
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  lateinit var viM: WeatherViewModel
  lateinit var adapter: WeatherToday
  val url: String = "http://openweathermap.org/img/w/10d.png"

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    viM = ViewModelProvider(this).get(WeatherViewModel::class.java)
    adapter = WeatherToday()
    val llayout: LinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    binding.recyclerView.layoutManager = llayout

    binding.searchCity.setOnEditorActionListener { _, actionId, event ->
      if (actionId == EditorInfo.IME_ACTION_DONE ||
        event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER
      ) {
        // Handle the enter key press here
        val enteredText = binding.searchCity.text.toString()
        viM.getWeather(enteredText)
        Toast.makeText(baseContext, "$enteredText", Toast.LENGTH_SHORT).show()
        // Do something with enteredText
        true
      } else {
        false
      }
    }


    viM.closetorexactlysameweatherdata.observe(this, Observer {
      val temperatureFahrenheit = it!!.main?.temp
      val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
      val temperatureFormatted = String.format("%.2f", temperatureCelsius)


      for (i in it.weather) {
        binding.layoutWeather.weatherType.text = i.description

      }

      binding.layoutWeather.weatherTemp.text = "$temperatureFormatted°"


      binding.layoutWeather.weatherHumidity.text = it.main!!.humidity.toString()
      binding.layoutWeather.weatherWind.text = it.wind?.speed.toString()
      binding.layoutWeather.weatherRain.text = it.clouds?.all.toString()
      val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
      val date = inputFormat.parse(it.dtTxt!!)
      val outputFormat = SimpleDateFormat("d MMMM EEEE", Locale.getDefault())
      val dateanddayname = outputFormat.format(date!!)
      binding.layoutWeather.weatherDate.text = dateanddayname.toString()
      // setting the icon
      for (i in it.weather) {
        if (i.icon == "01d") {
          Glide.with(binding.layoutWeather.weatherImage.context)
            .load(R.drawable.oned)
            .into(binding.layoutWeather.weatherImage)

        }

        if (i.icon == "01n") {
          Glide.with(binding.layoutWeather.weatherImage.context)
            .load(R.drawable.onen)
            .into(binding.layoutWeather.weatherImage)

        }

        if (i.icon == "02d") {
          Glide.with(binding.layoutWeather.weatherImage.context)
            .load(R.drawable.twod)
            .into(binding.layoutWeather.weatherImage)

        }


        if (i.icon == "02n") {
          Glide.with(binding.layoutWeather.weatherImage.context)
            .load(R.drawable.twon)
            .into(binding.layoutWeather.weatherImage)

        }


        if (i.icon == "03d" || i.icon == "03n") {
          Glide.with(binding.layoutWeather.weatherImage.context)
            .load(R.drawable.threedn)
            .into(binding.layoutWeather.weatherImage)

        }



        if (i.icon == "10d") {
          Glide.with(binding.layoutWeather.weatherImage.context)
            .load(R.drawable.tend)
            .into(binding.layoutWeather.weatherImage)

        }


        if (i.icon == "10n") {
          Glide.with(binding.layoutWeather.weatherImage.context)
            .load(R.drawable.tenn)
            .into(binding.layoutWeather.weatherImage)

        }


        if (i.icon == "04d" || i.icon == "04n") {
          Glide.with(binding.layoutWeather.weatherImage.context)
            .load(R.drawable.fourdn)
            .into(binding.layoutWeather.weatherImage)

        }


        if (i.icon == "09d" || i.icon == "09n") {
          Glide.with(binding.layoutWeather.weatherImage.context)
            .load(R.drawable.ninedn)
            .into(binding.layoutWeather.weatherImage)

        }



        if (i.icon == "11d" || i.icon == "11n") {
          Glide.with(binding.layoutWeather.weatherImage.context)
            .load(R.drawable.elevend)
            .into(binding.layoutWeather.weatherImage)

        }


        if (i.icon == "13d" || i.icon == "13n") {
          Glide.with(binding.layoutWeather.weatherImage.context)
            .load(R.drawable.thirteend)
            .into(binding.layoutWeather.weatherImage)

        }

        if (i.icon == "50d" || i.icon == "50n") {
          Glide.with(binding.layoutWeather.weatherImage.context)
            .load(R.drawable.fiftydn)
            .into(binding.layoutWeather.weatherImage)

        }

      }
    })

    viM.todayWeatherLiveData.observe(this, Observer {
      val setNewlist = it as List<WeatherList>


      adapter.setList(setNewlist)
      binding.recyclerView.adapter = adapter
    })

  }
}