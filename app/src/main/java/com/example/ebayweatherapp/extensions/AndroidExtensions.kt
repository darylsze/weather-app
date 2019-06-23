package com.example.ebayweatherapp.extensions

import android.content.SharedPreferences
import android.util.Log
import com.example.ebayweatherapp.constants.SEARCH_HISTORIES
import com.example.ebayweatherapp.retrofit.response.WeatherResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.autoNetworkThread(): Observable<T> {
    return this.subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
}

fun SharedPreferences.getSearchHistories(gson: Gson): List<WeatherResponse> {
    val json = getString(SEARCH_HISTORIES, "")
    return try {
        gson.fromJson(json, object : TypeToken<List<WeatherResponse>>() {}.type)
    } catch (e: Throwable) {
        Log.e("getSearchHistories", e.localizedMessage)
        listOf()
    }
}

fun SharedPreferences.setSearchHistories(gson: Gson, histories: List<WeatherResponse>): List<WeatherResponse> {
    edit().putString(SEARCH_HISTORIES, gson.toJson(histories)).apply()
    return histories
}