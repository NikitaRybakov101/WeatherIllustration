package com.example.weatherpaint.repository.retrofit

import com.google.gson.annotations.SerializedName

data class ResponseDateOneCall(
    @field:SerializedName("hourly") val listHourly : List<Hourly>?,
    @field:SerializedName("daily")  val days : List<Day>?
)

data class Hourly(
    @field:SerializedName("temp")    val temp : String,
    @field:SerializedName("dt")      val unixDT  : String,
    @field:SerializedName("weather") val weather : List<WeatherHourly>
)

data class WeatherHourly(
    @field:SerializedName("main") val weather : String,
    @field:SerializedName("icon") val icon    : String
)

data class Day(
    @field:SerializedName("temp")       val tempDay    : TempDay,
    @field:SerializedName("dt")         val unixDT     : String,
    @field:SerializedName("humidity")   val humidity   : String,
    @field:SerializedName("wind_speed") val wind_speed : String,
    @field:SerializedName("pressure")   val pressure   : String,
    @field:SerializedName("weather")    val weather    : List<WeatherDay>
)

data class TempDay(
    @field:SerializedName("morn")  val morn  : String,
    @field:SerializedName("day")   val day   : String,
    @field:SerializedName("eve")   val eve   : String,
    @field:SerializedName("night") val night : String
)

data class WeatherDay(
    @field:SerializedName("main") val weather : String,
    @field:SerializedName("icon") val icon    : String
)








