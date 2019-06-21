package com.example.ebayweatherapp

import android.app.Application
import com.example.ebayweatherapp.retrofit.service.WeatherService
import com.example.ebayweatherapp.retrofit.service.WeatherServiceImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class OpenWeatherAppIdInterceptor : Interceptor {
    var appid: String = ""

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().let {
            val newUrl = it.url().newBuilder().addQueryParameter("appid", appid).build()
            it.newBuilder().url(newUrl).build()
        }
        return chain.proceed(request)
    }
}

class Application : Application(), KodeinAware {
    // di with context and lots of android service
    override val kodein by Kodein.lazy {
        import(androidXModule(this@Application))

        bind<OkHttpClient>() with singleton {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
                .addInterceptor(OpenWeatherAppIdInterceptor().apply { appid = "95d190a434083879a6398aafd54d9e73" })
                .build()
        }

        bind<Retrofit>() with singleton {
            Retrofit.Builder()
                .client(instance() as OkHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://samples.openweathermap.org/data/2.5")
                .build()
        }

        bind<WeatherService>() with singleton {
            WeatherServiceImpl(instance() as Retrofit)
        }

        bind<Gson>() with singleton {
            GsonBuilder()
//                .registerTypeAdapter(Instant::class.java, InstantTypeConverter())
                .setLenient().create()
        }
    }

}