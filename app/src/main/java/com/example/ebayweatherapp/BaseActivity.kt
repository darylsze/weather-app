package com.example.ebayweatherapp

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware

open class BaseActivity: AppCompatActivity(), KodeinAware {
    override val kodein by lazy { (applicationContext as Application).kodein }
    val compositeDisposable = CompositeDisposable()

    override fun onPause() {
        super.onPause()

        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

    override fun onStop() {
        super.onStop()

        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}