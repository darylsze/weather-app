package com.example.ebayweatherapp.viewModel

import android.content.Context
import com.example.ebayweatherapp.R
import com.example.ebayweatherapp.SearchHistory
import com.example.ebayweatherapp.extensions.getSearchHistories
import com.example.ebayweatherapp.extensions.setSearchHistories
import com.example.ebayweatherapp.extensions.slientError
import com.example.ebayweatherapp.retrofit.response.WeatherResponse
import com.google.gson.Gson
import io.reactivex.Observable
import org.jetbrains.anko.defaultSharedPreferences

class SummaryViewModel(
        private val stream: Observable<WeatherResponse>
) {
    companion object {
        fun of(stream: Observable<WeatherResponse>): SummaryViewModel {
            return SummaryViewModel(stream)
        }
    }

    // https://openweathermap.org/weather-conditions
    private val weatherIconMapper = mapOf(
            "Clouds" to R.drawable.cloudy,
            "Thunderstorm" to R.drawable.storm,
            "Drizzle" to R.drawable.rain,
            "Rain" to R.drawable.rain,
            "Clear" to R.drawable.sun
    )

    fun getWeatherIcon(): Observable<Int> {
        return stream
                .map { response ->
                    response
                            .weather
                            .firstOrNull()
                            ?.main
                            ?.let { name -> weatherIconMapper.getOrElse(name, { R.drawable.na }) }
                            ?: R.drawable.na
                }
                .slientError()
    }

    fun getCurrentWeatherByLocation(): Observable<String> {
        // FIXME
        return stream.map { it.toString() }.slientError()
    }

    fun getLocationName(): Observable<String> {
        return stream.map { it.name }.slientError()
    }

    fun getCountryName(): Observable<String> {
        return stream.map { ", ${it.sys.country}" }.slientError()
    }

    fun getTemp(): Observable<String> {
        return stream
                .map {
                    // convert from kelvin to celsius
                    val cTemp = it.main.temp - 272.15
                    val displayCTemp = String.format("%.1f", cTemp)
                    "$displayCTemp°C"
                }
                .slientError()

    }

    fun getHumidity(): Observable<String> {
        return stream
                .map { "${it.main.humidity}%" }
                .slientError()
    }

    fun getVisibility(): Observable<String> {
        return stream
                .map {
                    val displayValue = it.visibility?.div(1000) ?: "--"
                    "${displayValue}k"
                }
                .slientError()
    }

    fun isWeatherInformationReady(): Observable<Boolean> {
        return stream.map { true }
    }

    fun getSearchHistories(context: Context, gson: Gson): List<SearchHistory> {
        return context.defaultSharedPreferences.getSearchHistories(gson)
    }

    fun addSearchHistory(context: Context, gson: Gson, history: SearchHistory): List<SearchHistory> {
        val prevHistories = context.defaultSharedPreferences.getSearchHistories(gson)
        val newHistories = prevHistories.plus(history)
        context.defaultSharedPreferences.setSearchHistories(gson, newHistories)
        return newHistories
    }
}