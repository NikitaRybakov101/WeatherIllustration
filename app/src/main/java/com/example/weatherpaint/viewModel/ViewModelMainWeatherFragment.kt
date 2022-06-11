package com.example.weatherpaint.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherpaint.repository.retrofit.RetrofitImpl
import com.example.weatherpaint.ui.fragments.other.Const.API_KEY
import com.example.weatherpaint.ui.fragments.other.Const.LOADING
import com.example.weatherpaint.ui.fragments.other.Const.NETWORK_ERROR
import com.example.weatherpaint.ui.fragments.other.Const.NOT_FOUND
import kotlinx.coroutines.*

class ViewModelMainWeatherFragment : ViewModel() {
    private val liveData = MutableLiveData<StateApp>()
    private val retrofit: RetrofitImpl = RetrofitImpl()

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        liveData.postValue(StateApp.Error(Throwable(NETWORK_ERROR)))
    }
    private val scope = CoroutineScope(Dispatchers.IO + exceptionHandler + SupervisorJob())
    private val apiKey = API_KEY

    fun getLiveData() = liveData

    fun sendServerWeather(lat: String, lon: String){
        liveData.value = StateApp.Loading(LOADING)

        val retrofit = retrofit.getRetrofit()

        scope.launch {
            val response = retrofit.getCurrentWeatherData(apiKey,lat, lon).execute()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    liveData.value = StateApp.Success(response.body()!!)
                } else {
                    liveData.value = StateApp.Error(Throwable(NOT_FOUND))
                }
            }
        }

        scope.launch {
            val responseOneCall = retrofit.getOneCallAPIAsync(apiKey, lat, lon).execute()

            withContext(Dispatchers.Main) {
                if (responseOneCall.isSuccessful && responseOneCall.body() != null) {
                    liveData.value = StateApp.SuccessOneCall(responseOneCall.body()!!)
                } else {
                    liveData.value = StateApp.Error(Throwable(NOT_FOUND))
                }
            }
        }
    }

    fun sendServerWeatherToAdd(lat: String, lon: String){
        val retrofit = retrofit.getRetrofit()

        scope.launch {
            val responseOneCall = retrofit.getOneCallAPIAsync(apiKey, lat, lon).execute()

            withContext(Dispatchers.Main) {
                if (responseOneCall.isSuccessful && responseOneCall.body() != null) {
                    liveData.value = StateApp.SuccessOneCallToAdd(responseOneCall.body()!!)
                } else {
                    liveData.value = StateApp.Error(Throwable(NOT_FOUND))
                }
            }
        }
    }

}