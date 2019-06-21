package com.example.ebayweatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware

open class BaseActivity: AppCompatActivity(), KodeinAware {
    final override val kodein by kodein()
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