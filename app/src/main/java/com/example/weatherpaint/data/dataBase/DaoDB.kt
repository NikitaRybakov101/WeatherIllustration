package com.example.weatherpaint.data.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DaoDB {
    @Query("SELECT * FROM day_weather")
    fun getAllDays() : List<EntitiesWeatherDay>

    @Query("SELECT * FROM day_weather where date = :date AND city = :city ")
    fun getDay(date : String, city : String) : List<EntitiesWeatherDay>

    @Query("SELECT * FROM day_weather WHERE dayT BETWEEN :min AND :max")
    fun getDayMinMax(min : Int,max : Int) : List<EntitiesWeatherDay>

    @Query("DELETE FROM day_weather WHERE id LIKE :id")
    fun deleteDay(id : Int) : Int

    @Query("DELETE FROM day_weather")
    fun deleteAll() : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDays(list : List<EntitiesWeatherDay>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDay(day : EntitiesWeatherDay)
}