package com.example.ebayweatherapp.viewModel

import android.content.Context
import com.example.ebayweatherapp.R
import com.example.ebayweatherapp.extensions.getSearchHistories
import com.example.ebayweatherapp.extensions.setSearchHistories
import com.example.ebayweatherapp.extensions.slientError
import com.example.ebayweatherapp.retrofit.response.WeatherResponse
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.jetbrains.anko.defaultSharedPreferences

class SummaryViewModel(
    val apiSignal: Observable<WeatherResponse>,
    val searchHistorySignal: Observable<List<WeatherResponse>>
) {
    // https://openweathermap.org/weather-conditions
    private val weatherIconMapper = mapOf(
        "Clouds" to R.drawable.cloudy,
        "Thunderstorm" to R.drawable.storm,
        "Drizzle" to R.drawable.rain,
        "Rain" to R.drawable.rain,
        "Clear" to R.drawable.sun
    )

    fun getWeatherIcon(): Observable<Int> {
        return apiSignal
            .switchIfEmpty(searchHistorySignal.map { it.first() })
            .doOnNext { println("getWeatherIcon result -> $it") }
            .map { it.weather.first().main }
            .map { weatherIconMapper.getOrElse(it, { R.drawable.na }) }
            .slientError()
    }

    fun getLocationName(): Observable<String> {
        return apiSignal
            .switchIfEmpty(searchHistorySignal.map { it.firstOrNull() })
            .doOnNext { println("in getLocationName -> $it") }
            .map { it.name }
            .slientError()
    }

    fun getCountryName(): Observable<String> {
        return apiSignal.map { ", ${it.sys.country}" }.slientError()
    }

    fun getTemp(): Observable<String> {
        return apiSignal
            .map {
                // convert from kelvin to celsius
                val cTemp = it.main.temp - 272.15
                val displayCTemp = String.format("%.1f", cTemp)
                "$displayCTemp°C"
            }
            .slientError()
    }

    fun getHumidity(): Observable<String> {
        return apiSignal
            .map { "${it.main.humidity}%" }
            .slientError()
    }

    fun getVisibility(): Observable<String> {
        return apiSignal
            .map {
                val displayValue = it.visibility?.div(1000) ?: "--"
                "${displayValue}k"
            }
            .slientError()
    }

    fun isWeatherInformationReady(): Observable<Boolean> {
        return searchHistorySignal
            .map { it.isNotEmpty() }
            .slientError()
    }

    fun getSearchHistories(context: Context, gson: Gson): List<WeatherResponse> {
        return context.defaultSharedPreferences.getSearchHistories(gson)
    }

    fun addSearchHistory(context: Context, gson: Gson, history: WeatherResponse): List<WeatherResponse> {
        val oldHistories = context.defaultSharedPreferences.getSearchHistories(gson)
        val newHistories = oldHistories.plus(history)
        return context.defaultSharedPreferences.setSearchHistories(gson, newHistories)
    }
}