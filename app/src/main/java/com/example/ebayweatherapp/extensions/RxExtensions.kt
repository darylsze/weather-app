package com.example.ebayweatherapp.extensions

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

infix fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun <T> Observable<T>.slientError(): Observable<T> {
    return this.onErrorResumeNext(Observable.empty())
}
