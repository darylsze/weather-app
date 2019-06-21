package com.example.ebayweatherapp

import android.os.Bundle
import com.example.ebayweatherapp.extensions.addTo
import com.example.ebayweatherapp.retrofit.service.WeatherService
import com.example.ebayweatherapp.viewModel.SummaryViewModel
import kotlinx.android.synthetic.main.weather_header.*
import org.kodein.di.generic.instance

class SummaryActivity : BaseActivity() {
    private val weatherService: WeatherService by instance()

    private val viewModel by lazy {
        val stream = weatherService.hitCountCheck('')
        SummaryViewModel.of(stream)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtLocation.text = "PARIS, FR"
        txtDate.text = "Sat Apr 21 2018"
        imageWeather.setImageResource(R.drawable.cloudy)
        txtTemp.text = "20 'C"
        txtHumidity.text = "52%"
        txtFeelTemp.text = "26 'C"

        viewModel
            .getCurrentWeatherByLocation()
            .subscribe { txtFeelTemp.text = it } addTo compositeDisposable
    }

}

