package com.example.weatherpaint.koinApp

import android.content.Context
import androidx.room.Room
import com.example.weatherpaint.data.dataBase.DataBase
import com.example.weatherpaint.viewModel.ViewModelMainWeatherFragment
import com.example.weatherpaint.viewModel.ViewModelWeekFragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object DI {
    val mainModule = module {

        single { (context : Context) ->
            Room.databaseBuilder(context, DataBase::class.java,"daysDB_3").build().dataBase()
        }

        viewModel { ViewModelMainWeatherFragment() }
        viewModel { ViewModelWeekFragment() }
    }
}