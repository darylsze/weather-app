package com.example.ebayweatherapp.utils

import com.example.ebayweatherapp.R
import org.junit.Test

import org.junit.Assert.*

class WeatherUtilKtTest {
    @Test
    fun shouldMapCloudsToCloudyDrawable() {
        assertEquals(weatherNameToIcon("Clouds"), R.drawable.cloudy)
    }

    @Test
    fun shouldMapThunderStormToStormDrawable() {
        assertEquals(weatherNameToIcon("Thunderstorm"), R.drawable.storm)
    }

    @Test
    fun shouldMapDrizzleToRainDrawable() {
        assertEquals(weatherNameToIcon("Drizzle"), R.drawable.rain)
    }

    @Test
    fun shouldMapClearToSunDrawable() {
        assertEquals(weatherNameToIcon("Clear"), R.drawable.sun)
    }

    @Test
    fun shouldMapRainToRainDrawable() {
        assertEquals(weatherNameToIcon("Rain"), R.drawable.rain)
    }
    @Test
    fun shouldMapUnhandledToSunDrawable() {
        assertEquals(weatherNameToIcon("unhandled"), R.drawable.sun)
    }
}