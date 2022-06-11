package com.example.weatherpaint.ui.fragments.weekWeatherFragment

import com.example.weatherpaint.R
import com.example.weatherpaint.repository.retrofit.Day

interface InterfaceWeekFragment {
    fun callBackRecycler(day: Day)
}