package com.example.weatherpaint.ui.fragments.weekWeatherFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.example.weatherpaint.R
import com.example.weatherpaint.databinding.DetailsWeekFragmentBinding
import com.example.weatherpaint.ui.fragments.main.MainWeatherFragment
import com.example.weatherpaint.ui.fragments.other.Const.C
import com.example.weatherpaint.ui.fragments.other.Const.CLEAR
import com.example.weatherpaint.ui.fragments.other.Const.CLOUDS
import com.example.weatherpaint.ui.fragments.other.Const.KEY_DETAILS
import com.example.weatherpaint.ui.fragments.other.Const.RAIN
import com.example.weatherpaint.ui.fragments.other.Const.SNOW
import com.example.weatherpaint.ui.fragments.other.Const.convertUnix
import com.example.weatherpaint.ui.fragments.other.Const.toCelsius
import kotlin.collections.ArrayList

class DetailsWeekFragment : Fragment() {
    private var _binding : DetailsWeekFragmentBinding? = null
    private val binding : DetailsWeekFragmentBinding get() =  _binding!!

    companion object {
        fun getDetailsFragment(data : DataDetailsWeather) : DetailsWeekFragment{
            val bundle = Bundle()
            bundle.putParcelable(KEY_DETAILS,data)

            val fragment = DetailsWeekFragment()
            fragment.arguments = bundle

            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DetailsWeekFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val day = arguments?.getParcelable<DataDetailsWeather>(KEY_DETAILS)

        day?.let { setGraphTemp(it) }
        binding.tempGraph.setStart(true,2f,false)

        day?.let { setDataDetails(it) }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataDetails(day : DataDetailsWeather) = with(binding){

        val list = getString(R.string.seasons).split(",")
        headerDetails.text = "${convertUnix(day.unixDT,"dd").toInt()} ${list[convertUnix(day.unixDT,"MM").toInt() - 1]}"

        textTempMor.text   = "${String.format("%.1f",toCelsius(day.mornT)) }$C"
        textTempDay.text   = "${String.format("%.1f",toCelsius(day.dayT))  }$C"
        textTempEve.text   = "${String.format("%.1f",toCelsius(day.eveT))  }$C"
        textTempNight.text = "${String.format("%.1f",toCelsius(day.nightT))}$C"

        humidity.text  = getString(R.string.humidity) +day.humidity + getString(R.string.persent)
        pressure.text  = getString(R.string.pressure) +day.pressure + getString(R.string.press)
        speedWind.text = getString(R.string.wind) +day.wind_speed + getString(R.string.speed)
        temp2.text     = getString(R.string.feels_like) +String.format("%.1f",toCelsius(day.dayT)) + C

        when(day.weather){
            CLEAR  -> { imageHeader.load(R.drawable.w_sun)    }
            CLOUDS -> { imageHeader.load(R.drawable.w_cloudy) }
            SNOW   -> { imageHeader.load(R.drawable.w_snow)   }
            RAIN   -> { imageHeader.load(R.drawable.w_rainy)  }
            else -> {}
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setGraphTemp(day : DataDetailsWeather){

        val list = ArrayList<MainWeatherFragment.HourlyTemp>()
        day.mornT.let  { list.add(MainWeatherFragment.HourlyTemp(toCelsius(it),""))}
        day.dayT.let   { list.add(MainWeatherFragment.HourlyTemp(toCelsius(it),""))}
        day.eveT.let   { list.add(MainWeatherFragment.HourlyTemp(toCelsius(it),""))}
        day.nightT.let { list.add(MainWeatherFragment.HourlyTemp(toCelsius(it),""))}

        binding.tempGraph.upDate(list)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}