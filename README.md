# weather-app
A demo weather app using RxJava2, AndroidX, Kodein, MVVM and OpenWeatherMap api

## It manages:
1. search weather for location
1. show weather information (temperature, humindity, visibility)
1. show search histories
1. auto refresh latest search history when app moves from background to foreground.

## Tech stack:
#### Networking:
1. Retrofit (annotation-based networking framework, and support Observable)
1. OkHttp3 (core of Retrofit)
1. Gson (json <-> string interpolation)

#### Dependency injection:
1. Kodein (easy config and use)

#### Framework:
1. RxJava2
1. RxKotlin
1. AndroidX (decouple dependency from android support library)
1. RxBinding (view binding to support Rx)

#### UI libraries:
1. recyclerview
1. constraintlayout
1. floatingsearchview
1. anko (handy syntactic-sugar for frequently used views, like, toast, dialog, runOnUiThread)

#### Design pattern:
1. MVVM (pure Rx, not android LifeData)

## Demo (GIF)
<image width="320" src="https://github.com/darylsze/weather-app/blob/master/demo/ebay-weather-ap.gif" />
