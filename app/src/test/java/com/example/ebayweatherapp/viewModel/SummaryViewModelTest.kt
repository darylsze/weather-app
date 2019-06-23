package com.example.ebayweatherapp.viewModel

import com.example.ebayweatherapp.fixtures.openWeatherSuccessResponseJSON
import com.example.ebayweatherapp.retrofit.response.WeatherResponse
import com.example.ebayweatherapp.utils.weatherNameToIcon
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Test

class SummaryViewModelTest {
    private val fakeSuccessResponseJson: String = openWeatherSuccessResponseJSON
    private val fakeWeatherResponse: WeatherResponse = Gson().fromJson(fakeSuccessResponseJson, WeatherResponse::class.java)

    @Test
    fun getWeatherIcon() {
        val testObserver = TestObserver<Int>()
        val apiSignal = Observable.just(fakeWeatherResponse)
        val searchHistorySignal = Observable.empty<List<WeatherResponse>>()

        SummaryViewModel(apiSignal, searchHistorySignal)
            .getWeatherIcon()
            .subscribe(testObserver)

        testObserver.assertValue(weatherNameToIcon("Clouds"))
    }

    @Test
    fun getLocationName() {
        val testObserver = TestObserver<String>()
        val apiSignal = Observable.just(fakeWeatherResponse)
        val searchHistorySignal = Observable.empty<List<WeatherResponse>>()

        SummaryViewModel(apiSignal, searchHistorySignal)
            .getLocationName()
            .subscribe(testObserver)

        testObserver.assertValue("Hong Kong")
    }

    @Test
    fun getCountryName() {
        val testObserver = TestObserver<String>()
        val apiSignal = Observable.just(fakeWeatherResponse)
        val searchHistorySignal = Observable.empty<List<WeatherResponse>>()

        SummaryViewModel(apiSignal, searchHistorySignal)
            .getCountryName()
            .subscribe(testObserver)

        testObserver.assertValue(", CN")
    }

    @Test
    fun getTempWhenApiReady() {
        val testObserver = TestObserver<String>()
        val apiSignal = Observable.just(fakeWeatherResponse)
        val searchHistorySignal = Observable.empty<List<WeatherResponse>>()

        SummaryViewModel(apiSignal, searchHistorySignal)
            .getTemp()
            .subscribe(testObserver)

        testObserver.assertValue("32.9°C")
    }

    @Test
    fun getTempWhenApiNotReady() {
        val testObserver = TestObserver<String>()
        val apiSignal = Observable.empty<WeatherResponse>()
        val searchHistorySignal = Observable.empty<List<WeatherResponse>>()

        SummaryViewModel(apiSignal, searchHistorySignal)
            .getTemp()
            .subscribe(testObserver)

        testObserver.assertComplete()
    }

    @Test
    fun getHumidity() {
        val testObserver = TestObserver<String>()
        val apiSignal = Observable.just(fakeWeatherResponse)
        val searchHistorySignal = Observable.empty<List<WeatherResponse>>()

        SummaryViewModel(apiSignal, searchHistorySignal)
            .getHumidity()
            .subscribe(testObserver)

        testObserver.assertValue("70.0%")
    }

    @Test
    fun getVisibility() {
        val testObserver = TestObserver<String>()
        val apiSignal = Observable.just(fakeWeatherResponse)
        val searchHistorySignal = Observable.empty<List<WeatherResponse>>()

        SummaryViewModel(apiSignal, searchHistorySignal)
            .getVisibility()
            .subscribe(testObserver)

        testObserver.assertValue("10k")
    }
}