package com.example.weatherpaint.viewModel

import com.example.weatherpaint.repository.retrofit.ResponseData
import com.example.weatherpaint.repository.retrofit.ResponseDateOneCall

sealed class StateApp {
    data class Success(val weather : ResponseData) : StateApp()
    data class SuccessOneCall(val weather : ResponseDateOneCall) : StateApp()
    data class SuccessOneCallToAdd(val weather : ResponseDateOneCall) : StateApp()

    data class Loading(val loading : String)     : StateApp()
    data class Error  (val error   : Throwable)  : StateApp()
}