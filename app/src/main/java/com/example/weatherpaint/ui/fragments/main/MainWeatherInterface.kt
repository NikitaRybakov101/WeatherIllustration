package com.example.weatherpaint.ui.fragments.main

interface MainWeatherInterface {
    fun cityFragmentClose()
    fun setLatLon(lat: String, lon: String, cityName: String)
}