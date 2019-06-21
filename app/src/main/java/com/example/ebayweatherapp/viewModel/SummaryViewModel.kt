package com.example.ebayweatherapp.viewModel

import com.example.ebayweatherapp.R
import com.example.ebayweatherapp.retrofit.response.WeatherResponse
import io.reactivex.Observable

class SummaryViewModel(
    private val stream: Observable<WeatherResponse>
) {
    companion object {
        fun of(stream: Observable<WeatherResponse>): SummaryViewModel {
            return SummaryViewModel(stream)
        }
    }

    private val weatherIconMapper = mapOf(
        "Clouds" to R.drawable.cloudy
    )

    fun getWeatherIcon(): Observable<Int> {
        return stream
            .map { response ->
                response
                    .weather
                    .firstOrNull()
                    ?.main
                    ?.let { name -> weatherIconMapper.getOrElse(name, { R.drawable.sun }) } ?: R.drawable.sun
            }
            .onErrorReturnItem(0)
    }

    fun getCurrentWeatherByLocation(): Observable<String> {
        // FIXME
        return stream.map { it.toString() }.onErrorReturnItem("")
    }

    fun getLocationName(): Observable<String> {
        return stream.map { it.name }.onErrorReturnItem("")
    }

    fun getCountryName(): Observable<String> {
        return stream.map { ", ${it.sys.country}" }.onErrorReturnItem("")
    }

    fun getTemp(): Observable<String> {
        return stream
            .map {
                // convert from kelvin to celsius
                val cTemp = it.main.temp - 272.15
                val displayCTemp = String.format("%.1f", cTemp)
                "$displayCTemp°C"
            }.onErrorReturnItem("")
    }

    fun getHumidity(): Observable<String> {
        return stream.map { "${it.main.humidity}%" }.onErrorReturnItem("")
    }

    fun getVisibility(): Observable<String> {
        return stream.map {
            val displayValue = it.visibility?.div(1000) ?: "--"
            "${displayValue}k"
        }.onErrorReturnItem("")
    }
}