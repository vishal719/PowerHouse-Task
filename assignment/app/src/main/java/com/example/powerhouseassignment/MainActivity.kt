package com.example.powerhouseassignment

import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.powerhouseassignment.adapter.WeatherToday
import com.example.powerhouseassignment.databinding.ActivityMainBinding
import com.example.powerhouseassignment.ui.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  lateinit var viM: WeatherViewModel
  lateinit var adapter: WeatherToday
  private lateinit var fusedLocationClient: FusedLocationProviderClient

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    viM = ViewModelProvider(this).get(WeatherViewModel::class.java)
    adapter = WeatherToday()
    val llayout: LinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    binding.recyclerView.layoutManager = llayout
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    binding.searchCity.setOnEditorActionListener { _, actionId, _ ->
      Log.d("Debug", "Action ID: $actionId")
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        val enteredText = binding.searchCity.text.toString()
        viM.getWeather(enteredText)
        Toast.makeText(baseContext, "$enteredText", Toast.LENGTH_SHORT).show()
        true
      } else {
        false
      }
    }

    viM.cityName.observe(this, Observer{
      binding.layoutWeather.weatherCity.text = it.toString()
    })

    viM.weatherLiveData.observe(this, Observer {
      val temperatureFahrenheit = it!!.main?.temp
      val temperatureCelsius = (temperatureFahrenheit?.minus(273.15))
      val temperatureFormatted = String.format("%.2f", temperatureCelsius)

      binding.layoutWeather.weatherCity.text

      for (i in it.weather) {
        binding.layoutWeather.weatherType.text = i.description

      }

      binding.layoutWeather.weatherTemp.text = "$temperatureFormatted°"


      binding.layoutWeather.weatherHumidity.text = "${it.main!!.humidity.toString()} %"
      binding.layoutWeather.weatherWind.text = "${ it.wind?.speed.toString() } m/s"
      binding.layoutWeather.weatherRain.text = "${ it.clouds?.all.toString() } mm"
      val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
      val date = inputFormat.parse(it.dtTxt!!)
      val outputFormat = SimpleDateFormat("d MMMM EEEE,  HH:mm", Locale.getDefault())
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

    getLastKnownLocation()

// Check for permission
  }


  @RequiresApi(Build.VERSION_CODES.O)
  private fun getLastKnownLocation() {
    if (ActivityCompat.checkSelfPermission(
        this,
        android.Manifest.permission.ACCESS_FINE_LOCATION
      ) == PackageManager.PERMISSION_GRANTED
    ) {
      // Permission is granted, request location updates
      fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
          location?.let {
            val latitude = location.latitude
            val longitude = location.longitude
            // Use latitude and longitude
            Toast.makeText(this, "Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_SHORT)
              .show()
            viM.getWeather(lat = latitude, lon = longitude)
          }
        }

    } else {
      // Request permission
      ActivityCompat.requestPermissions(
        this,
        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
        REQUEST_LOCATION_PERMISSION
      )
    }
  }
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == REQUEST_LOCATION_PERMISSION) {
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // Permission granted, request location updates
        getLastKnownLocation()
      } else {
        // Permission denied, handle accordingly
      }
    }
  }

  companion object {
    private const val REQUEST_LOCATION_PERMISSION = 1001
  }

}