package com.example.weatherpaint.data.dataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "day_weather")
data class EntitiesWeatherDay(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,

    @ColumnInfo(name = "mornT") val mornT: String,
    @ColumnInfo(name = "dayT")  val dayT: String,
    @ColumnInfo(name = "eveT") val eveT: String,
    @ColumnInfo(name = "nightT") val nightT: String,

    @ColumnInfo(name = "unixDT") val unixDT: String,
    @ColumnInfo(name = "humidity") val humidity: String,
    @ColumnInfo(name = "wind_speed") val wind_speed: String,
    @ColumnInfo(name = "pressure") val pressure: String,

    @ColumnInfo(name = "weather") val weather: String,

    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "city") val city: String
)
