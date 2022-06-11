package com.example.weatherpaint.repository.retrofit

import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("data/2.5/weather")
    fun getCurrentWeatherData( @Query("appid") apiKey : String,
                               @Query("lat")   lat    : String,
                               @Query("lon")   lon    : String, ): Call<ResponseData>

    @GET("data/2.5/onecall")
    fun getOneCallAPIAsync(@Query("appid") apiKey : String,
                           @Query("lat")   lat    : String,
                           @Query("lon")   lon    : String, ): Call<ResponseDateOneCall>
}