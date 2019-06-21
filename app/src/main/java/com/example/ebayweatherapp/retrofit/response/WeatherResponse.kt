package com.example.ebayweatherapp.retrofit.response

data class WeatherResponse(
    val clouds: Clouds,
    val cod: Int, // 200
    val dt: Int, // 1560350645
    val main: Main,
    val name: String, // Mountain View
    val sys: Sys,
    val timezone: Int, // -25200
    val weather: List<Weather>,
    val wind: Wind
) {
    data class Clouds(
        val all: Int // 1
    )

    data class Main(
        val humidity: Double, // 53
        val pressure: Double, // 1013
        val temp: Double, // 296.71
        val temp_max: Double, // 298.71
        val temp_min: Double // 294.82
    )

    data class Sys(
        val country: String, // US
        val id: Int, // 5122
        val message: Double, // 0.0139
        val sunrise: Int, // 1560343627
        val sunset: Int, // 1560396563
        val type: Int // 1
    )

    data class Wind(
        val deg: Double, // 350
        val speed: Double // 1.5
    )

    data class Weather(
        val description: String, // clear sky
        val icon: String, // 01d
        val id: Int, // 800
        val main: String // Clear
    )
}