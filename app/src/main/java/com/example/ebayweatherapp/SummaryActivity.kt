package com.example.ebayweatherapp

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ebayweatherapp.extensions.addTo
import com.example.ebayweatherapp.extensions.slientError
import com.example.ebayweatherapp.retrofit.response.WeatherResponse
import com.example.ebayweatherapp.retrofit.service.WeatherService
import com.example.ebayweatherapp.rxbinding.queryChanges
import com.example.ebayweatherapp.utils.weatherNameToIcon
import com.example.ebayweatherapp.viewModel.SummaryViewModel
import com.google.gson.Gson
import com.jakewharton.rxbinding3.swiperefreshlayout.refreshes
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.empty_weather_summary.*
import kotlinx.android.synthetic.main.rv_row_history_weather.view.*
import kotlinx.android.synthetic.main.weather_header.*
import org.kodein.di.generic.instance
import java.util.*
import java.util.concurrent.TimeUnit

data class SearchHistory(
    val input: String,
    val weather: String,
    val createdAt: Date
)

// 1. got error dont resubmit signal
// 2. need check history for initial

class SummaryActivity : BaseActivity() {
    private val weatherService: WeatherService by instance<WeatherService>()
    private val gson: Gson by instance<Gson>()

    // track location or zip code
    private val locationSignal = BehaviorSubject.create<String>()

    // track API response
    private val apiSignal by lazy {
        BehaviorSubject.create<WeatherResponse>()
    }

    // track GPS
    private val gpsSignal = BehaviorSubject.create<Pair<Double, Double>>()

    private val previousSearchHistory by lazy {
        viewModel.getSearchHistories(this@SummaryActivity, gson)
    }

    private val searchHistorySignal = BehaviorSubject.create<List<SearchHistory>>()

    private val viewModel by lazy {
        // keep view model change based on API response
        SummaryViewModel(apiSignal, searchHistorySignal)
    }


    private val rvSearchHistoryAdapter by lazy {
        MyRvSearchResultAdapter(mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()

        searchHistorySignal.onNext(previousSearchHistory)

        searchHistorySignal
            .doOnNext { rvSearchHistoryAdapter.setHistories(it) }
            .subscribe() addTo compositeDisposable

        // cancel any refresh state when no network worker is active
        apiSignal
            .doOnNext { runOnUiThread { srl.isRefreshing = false } }
            .doOnNext {
                println(it)
                val history = SearchHistory(
                    input = it.name,
                    weather = it.weather.first().main,
                    createdAt = Date()
                )
                val result = viewModel.addSearchHistory(this@SummaryActivity, gson, history)
                searchHistorySignal.onNext(result)
            }
            .subscribe() addTo compositeDisposable

        // swipe refresh layout
        srl.refreshes()
            .doOnNext { locationSignal.onNext(locationSignal.value ?: "") }
            .slientError()
            .subscribe() addTo compositeDisposable

        // should show empty layout or not
        viewModel
            .isWeatherInformationReady()
            .doOnNext { runOnUiThread { lblEmptyWeatherInfo.isVisible = false } }
            .subscribe() addTo compositeDisposable

        // subscribe to location signal
        locationSignal
            .doOnNext { runOnUiThread { srl.isRefreshing = true } }
            .flatMap { weatherService.getWeatherByLocation(it) }
            .doOnError(::println) // UI
            .doOnNext { apiSignal.onNext(it) }
            .slientError()
            .subscribe() addTo compositeDisposable

        // icon
        viewModel
            .getWeatherIcon()
            .subscribe {
                imageWeather.setImageResource(it)
            } addTo compositeDisposable

        // location
        viewModel.getLocationName()
            .subscribe { txtLocation.text = it } addTo compositeDisposable

        // country
        viewModel.getCountryName()
            .subscribe { txtCountry.text = it } addTo compositeDisposable

        // visibility
        viewModel.getVisibility()
            .subscribe { txtVisibility.text = it } addTo compositeDisposable

        // humidity
        viewModel.getHumidity()
            .subscribe { txtHumidity.text = it } addTo compositeDisposable

        // temp
        viewModel.getTemp()
            .subscribe { txtTemp.text = it } addTo compositeDisposable

        // search view
        searchView.queryChanges()
            .debounce(2, TimeUnit.SECONDS)
            .filter {
                val (_, newValue) = it
                !TextUtils.isEmpty(newValue)
            }
            .doOnNext { locationSignal.onNext(it.second) }
            .subscribe() addTo compositeDisposable
    }

    private fun setupRecyclerView() {
        rvCountries.apply {
            layoutManager = LinearLayoutManager(this@SummaryActivity)
            adapter = rvSearchHistoryAdapter
        }
    }
}

class MyRvSearchResultAdapter(
    val dataset: MutableList<SearchHistory>
) : RecyclerView.Adapter<SearchHistoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryViewHolder {
        val view =
            (parent.context as BaseActivity).layoutInflater.inflate(R.layout.rv_row_history_weather, parent, false)
        return SearchHistoryViewHolder(view)
    }

    override fun getItemCount(): Int = dataset.size - 1

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        println(dataset[position])
        weatherNameToIcon(dataset[position].weather).apply {
            holder.itemView.imageWeather.setImageResource(this)
        }
        holder.itemView.lblLocation.text = dataset[position].input
        holder.itemView.lblLastUpdate.text = "abcd"
    }

    fun setHistories(histories: List<SearchHistory>) {
        dataset.clear()
        dataset.addAll(0, histories.reversed())
        this.notifyItemChanged(0)
    }

}

class SearchHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view)
