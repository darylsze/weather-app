package com.example.ebayweatherapp.fixtures

val openWeatherSuccessResponseJSON = """
    {
        "coord": {
        "lon": 114.16,
        "lat": 22.28
    },
    "weather": [
        {
            "id": 802,
            "main": "Clouds",
            "description": "scattered clouds",
            "icon": "03d"
        }
    ],
    "base": "stations",
    "main": {
        "temp": 305.08,
        "pressure": 1004,
        "humidity": 70,
        "temp_min": 304.15,
        "temp_max": 306.48
    },
    "visibility": 10000,
    "wind": {
        "speed": 7.2,
        "deg": 230
    },
    "clouds": {
        "all": 40
    },
    "dt": 1561272191,
    "sys": {
        "type": 1,
        "id": 9154,
        "message": 0.0083,
        "country": "CN",
        "sunrise": 1561239628,
        "sunset": 1561288217
    },
    "timezone": 28800,
    "id": 1819729,
    "name": "Hong Kong",
    "cod": 200
}
""".trimIndent()