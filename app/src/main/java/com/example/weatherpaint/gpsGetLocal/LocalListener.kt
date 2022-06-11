package com.example.weatherpaint.gpsGetLocal

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.widget.Toast
import com.example.weatherpaint.R
import com.example.weatherpaint.data.sharedPreference.SharedPreference
import com.example.weatherpaint.ui.activity.MainActivity
import com.example.weatherpaint.ui.fragments.other.Const
import com.example.weatherpaint.ui.fragments.other.Const.ERROR
import com.example.weatherpaint.ui.fragments.other.Const.LAT
import com.example.weatherpaint.ui.fragments.other.Const.LON

class LocalListener(private val context: Context) : LocationListener {

    override fun onLocationChanged(location: Location) {
        Toast.makeText(context,context.getString(R.string.positions) + "$LAT${location.latitude.toFloat()} / $LON${location.longitude.toFloat()}", Toast.LENGTH_SHORT).show()
        SharedPreference.saveDataLocation("$LAT${String.format("%.1f",location.latitude)} $LON${String.format("%.1f",location.longitude)};${location.latitude};${location.longitude}", Const.KEY_CITY, context as MainActivity)
    }

    override fun onProviderDisabled(provider: String) {
        Toast.makeText(context,ERROR,Toast.LENGTH_SHORT).show()
    }
    override fun onProviderEnabled(provider: String) {}
}