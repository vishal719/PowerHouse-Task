package com.example.powerhouseassignment.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.powerhouseassignment.MyApplication
import com.example.powerhouseassignment.repository.WeatherRepository

@Suppress("UNCHECKED_CAST")
class WeatherViewModelProviderFactory(val app: Application, val weatherRepository: WeatherRepository) : ViewModelProvider.Factory
{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel(app, weatherRepository) as T
    }

}