package com.example.ebayweatherapp

import android.os.Bundle
import com.example.ebayweatherapp.extensions.addTo
import com.example.ebayweatherapp.retrofit.service.WeatherService
import com.example.ebayweatherapp.viewModel.SummaryViewModel
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.weather_header.*
import org.kodein.di.generic.instance

class SummaryActivity : BaseActivity() {
    private val weatherService: WeatherService by instance()

    private val viewModel by lazy {
        val stream = weatherService.getWeatherByLocation("hong kong")
        SummaryViewModel.of(stream)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        txtDate.text = "Sat Apr 21 2018"

        viewModel
            .getWeatherIcon()
            .subscribe {
                imageWeather.setImageResource(it)
            } addTo compositeDisposable

        txtTemp.text = "20 'C"

        viewModel
            .getCurrentWeatherByLocation()
            .subscribe(Consumer { txtFeelTemp.text = it }, Consumer { print("error iin fetching api") }) addTo compositeDisposable

        // location
        viewModel.getLocationName()
            .subscribe { txtLocation.text = it } addTo compositeDisposable


        // country
        viewModel.getCountryName()
            .subscribe { txtCountry.text = it } addTo compositeDisposable

        // feelTemp
        viewModel.getVisibility()
            .subscribe { txtFeelTemp.text = it } addTo compositeDisposable

        viewModel.getHumidity()
            .subscribe { txtHumidity.text = it } addTo compositeDisposable

        viewModel.getTemp()
            .subscribe { txtTemp.text = it } addTo compositeDisposable
    }

}

