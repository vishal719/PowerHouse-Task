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
import com.bumptech.glide.Glide
import com.example.powerhouseassignment.databinding.ActivityMainBinding
import com.example.powerhouseassignment.ui.WeatherViewModel
import java.net.URI
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  lateinit var viM: WeatherViewModel

  val url : String = "http://openweathermap.org/img/w/10d.png"
  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    viM = ViewModelProvider(this).get(WeatherViewModel::class.java)


    binding.searchCity.setOnEditorActionListener { _, actionId, event ->
      if (actionId == EditorInfo.IME_ACTION_DONE ||
        event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER
      ) {
        // Handle the enter key press here
        val enteredText = binding.searchCity.text.toString()
        viM.getWeather(enteredText)
        Toast.makeText(baseContext,"$enteredText",Toast.LENGTH_SHORT).show()

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
      binding.layoutWeather.weatherRain.text = it.clouds.toString()

      val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
      val date = inputFormat.parse(it.dtTxt!!)
      val outputFormat = SimpleDateFormat("d MMMM EEEE", Locale.getDefault())
      val dateanddayname = outputFormat.format(date!!)
      binding.layoutWeather.weatherDate.text = dateanddayname.toString()

      Glide.with(binding.layoutWeather.weatherImage.context)
        .load(url)
        .into(binding.layoutWeather.weatherImage)

      // setting the icon
    })

  }
}