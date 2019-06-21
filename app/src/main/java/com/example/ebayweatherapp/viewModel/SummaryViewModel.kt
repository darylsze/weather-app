package com.example.ebayweatherapp.viewModel

import com.example.ebayweatherapp.retrofit.response.ApiResponse
import io.reactivex.Observable

class SummaryViewModel(
    private val stream: Observable<ApiResponse.Result>
) {
    companion object {
        fun of(stream: Observable<ApiResponse.Result>): SummaryViewModel {
            return SummaryViewModel(stream)
        }
    }

    fun getCurrentWeatherByLocation(): Observable<String> {
        return stream.map { it.query }.map { it.toString() }
    }
}