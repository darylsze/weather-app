package com.example.ebayweatherapp.retrofit.service

import com.example.ebayweatherapp.retrofit.response.ApiResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherServiceI : IBaseService {
    @GET("/weather")
    fun getWeatherByLocation(
        @Query("q") location: String
    ): Observable<>

    @GET("/weather")
    fun getWeatherByGPS(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Observable<>
}

class WeatherServiceImplI(retrofit: Retrofit) : WeatherServiceI {
    private val apiService = retrofit.create(WeatherServiceI::class.java)

    override fun hitCountCheck(
        action: String,
        format: String,
        list: String,
        srsearch: String
    ): Observable<ApiResponse.Result> {
        return apiService.hitCountCheck('').autoNetworkThread()
    }
}

