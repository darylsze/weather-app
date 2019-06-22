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
    private val apiSignal = BehaviorSubject.create<WeatherResponse>()

    // track GPS
    private val gpsSignal = BehaviorSubject.create<Pair<Double, Double>>()


    private val previousSearchHistory by lazy {
        viewModel.getSearchHistories(this@SummaryActivity, gson)
    }

    private val searchHistorySignal by lazy {
        BehaviorSubject.createDefault<List<SearchHistory>>(previousSearchHistory)
    }

    private val viewModel by lazy {
        // keep view model change based on API response
        SummaryViewModel.of(apiSignal)
    }


    private val rvSearchHistoryAdapter by lazy {
        MyRvSearchResultAdapter(mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()

        searchHistorySignal
            .doOnNext { rvSearchHistoryAdapter.dataset.addAll(it) }
            .subscribe()

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
        val view = (parent.context as BaseActivity).layoutInflater.inflate(R.layout.rv_row_history_weather, parent, false)
        return SearchHistoryViewHolder(view)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        holder.itemView.imageWeather.setImageResource(R.drawable.summer)
        holder.itemView.lblLocation.text = dataset[position].input
        holder.itemView.lblLastUpdate.text = "abcd"
    }

}

class SearchHistoryViewHolder(view: View): RecyclerView.ViewHolder(view)
