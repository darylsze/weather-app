package com.example.ebayweatherapp.rxbinding

import com.arlib.floatingsearchview.FloatingSearchView
import io.reactivex.Observable


fun FloatingSearchView.queryChanges(): Observable<Pair<String, String>> {
    return Observable.create {
        this.setOnQueryChangeListener { oldQuery, newQuery ->
            it.onNext(oldQuery to newQuery)
        }
    }
}

