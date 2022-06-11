package com.example.weatherpaint.data.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EntitiesWeatherDay::class], version = 3, exportSchema = false)
abstract class DataBase : RoomDatabase() {
    abstract fun dataBase() : DaoDB
}