package com.example.weatherpaint.repository.retrofit

import com.google.gson.annotations.SerializedName

data class ResponseData (
    @field:SerializedName("main")    val main: MainWeather?,
    @field:SerializedName("weather") val weather: ArrayList<Weather>?,
    @field:SerializedName("wind")    val wind: Wind?
)

data class MainWeather(
    @field:SerializedName("temp") val temp: String?,
    @field:SerializedName("humidity") val humidity: String?,
    @field:SerializedName("pressure") val pressure: String?,
    @field:SerializedName("feels_like") val feels_like: String
)

data class Weather(
    @field:SerializedName("main") val weatherDescription: String?
)

data class Wind(
    @field:SerializedName("speed") val speedWind: String?
)




