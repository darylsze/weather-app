package com.example.ebayweatherapp.viewModel

import android.content.Context
import androidx.core.view.isVisible
import com.example.ebayweatherapp.R
import com.example.ebayweatherapp.SearchHistory
import com.example.ebayweatherapp.extensions.getSearchHistories
import com.example.ebayweatherapp.extensions.setSearchHistories
import com.example.ebayweatherapp.extensions.slientError
import com.example.ebayweatherapp.retrofit.response.WeatherResponse
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.empty_weather_summary.*
import org.jetbrains.anko.defaultSharedPreferences

class SummaryViewModel(
        val apiSignal: Observable<WeatherResponse>,
        val searchHistorySignal: Observable<List<SearchHistory>>
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
        return apiSignal.map { it.toString() }.slientError()
    }

    fun getLocationName(): Observable<String> {
        return apiSignal.map { it.name }.slientError()
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
            .map{ it.isEmpty() }
            .slientError()
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