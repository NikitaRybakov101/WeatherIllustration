package com.example.weatherpaint.ui.fragments.weekWeatherFragment

import android.os.Parcel
import android.os.Parcelable

data class DataDetailsWeather(
    val mornT: String,
    val dayT: String,
    val eveT: String,
    val nightT: String,

    val unixDT: String,
    val humidity: String,
    val wind_speed: String,
    val pressure: String,

    val weather: String?    ) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mornT)
        parcel.writeString(dayT)
        parcel.writeString(eveT)
        parcel.writeString(nightT)
        parcel.writeString(unixDT)
        parcel.writeString(humidity)
        parcel.writeString(wind_speed)
        parcel.writeString(pressure)
        parcel.writeString(weather)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataDetailsWeather> {
        override fun createFromParcel(parcel: Parcel): DataDetailsWeather {
            return DataDetailsWeather(parcel)
        }

        override fun newArray(size: Int): Array<DataDetailsWeather?> {
            return arrayOfNulls(size)
        }
    }
}


