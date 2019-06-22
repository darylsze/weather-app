package com.example.ebayweatherapp.utils

import com.example.ebayweatherapp.R

private val weatherIconMapper = mapOf(
    "Clouds" to R.drawable.cloudy,
    "Thunderstorm" to R.drawable.storm,
    "Drizzle" to R.drawable.rain,
    "Rain" to R.drawable.rain,
    "Clear" to R.drawable.sun
)

fun weatherNameToIcon(name: String): Int {
    println(name)
    return weatherIconMapper
        .entries
        .firstOrNull { it.key == name}
        ?.value
        ?: R.drawable.sun
}