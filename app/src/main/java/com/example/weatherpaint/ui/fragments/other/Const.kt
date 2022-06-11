package com.example.weatherpaint.ui.fragments.other

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

object Const {
    const val CLEAR = "Clear"
    const val CLOUDS = "Clouds"
    const val SNOW = "Snow"
    const val RAIN = "Rain"

    const val TEMP_C = "TEMP_C"
    const val CHIP_D = "CHIP_D"
    const val CHIP_N = "CHIP_N"

    const val C = "c°"
    const val F = "F°"

    const val KEY_CITY = "optionCity"
    const val SEARCH = "SEARCH"

    const val KEY_DETAILS = "KEY_DETAILS"
    const val STACK = "STACK"

    const val NETWORK_ERROR = "netWork ERROR"
    const val ERROR = "GPS ERROR"
    const val NOT_FOUND = "not found"
    const val LOADING = "loading"

    const val FORMAT = "yyyy-MM-dd"

    const val NIGHT_THEME = "NIGHT_THEME"
    const val FIRST = "FIRST"

    const val START_DATA_LOCATIONS = "Moscow;55.75;37.62;"

    const val BASE_URL_API = "https://api.openweathermap.org/"

    const val FILE_NAME_CITY = "city_lat_lon.txt"

    const val START_CITY = "Moscow"
    const val START_LAT = "55.7416"
    const val START_LON = "37.6255"

    const val LON = "Lon"
    const val LAT = "Lat"

    const val API_KEY = "397c7efeb6694e2ead32a93e2d038d0a"

    @SuppressLint("SimpleDateFormat")
    fun convertUnix(unix : String, format : String) : String{

        val date = Date(unix.toLong() * 1000)
        return SimpleDateFormat(format).format(date)
    }
    fun toCelsius(temp : String) = temp.toFloat() - 273.15f
}