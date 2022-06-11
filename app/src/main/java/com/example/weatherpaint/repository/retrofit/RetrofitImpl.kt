package com.example.weatherpaint.repository.retrofit

import com.example.weatherpaint.ui.fragments.other.Const.BASE_URL_API
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitImpl {
    private val baseUrl = BASE_URL_API

    fun getRetrofit() : RetrofitInterface{
        val retrofit = Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
        return retrofit.create(RetrofitInterface::class.java)
    }
}