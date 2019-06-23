package com.example.ebayweatherapp.retrofit.service

import com.example.ebayweatherapp.extensions.autoNetworkThread
import com.example.ebayweatherapp.retrofit.response.WeatherResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherService {
    @GET("/data/2.5/weather")
    fun getWeatherByLocation(
        @Query("q") location: String
    ): Observable<WeatherResponse>

    @GET("/data/2.5/weather")
    fun getWeatherByGPS(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Observable<WeatherResponse>
}

class WeatherServiceImplI(private val apiService: WeatherService) : WeatherService {
    override fun getWeatherByLocation(location: String): Observable<WeatherResponse> {
        return apiService.getWeatherByLocation(location).autoNetworkThread()
    }

    override fun getWeatherByGPS(lat: String, lon: String): Observable<WeatherResponse> {
        return apiService.getWeatherByGPS(lat, lon).autoNetworkThread()
    }
}

