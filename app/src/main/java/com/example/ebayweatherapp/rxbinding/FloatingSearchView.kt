package com.example.ebayweatherapp.rxbinding

import android.view.MenuItem
import com.arlib.floatingsearchview.FloatingSearchView
import io.reactivex.Observable


fun FloatingSearchView.queryChanges(): Observable<Pair<String, String>> {
    return Observable.create {
        this.setOnQueryChangeListener { oldQuery, newQuery ->
            it.onNext(oldQuery to newQuery)
        }
    }
}

fun FloatingSearchView.menuItemChanges(): Observable<MenuItem> {
    return Observable.create<MenuItem> { emitter ->
        this.setOnMenuItemClickListener { menuItem ->
            emitter.onNext(menuItem)
        }
    }
}

