package com.example.weatherpaint.ui.fragments.main

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import coil.load
import com.example.weatherpaint.R
import com.example.weatherpaint.data.dataBase.DaoDB
import com.example.weatherpaint.data.dataBase.EntitiesWeatherDay
import com.example.weatherpaint.data.sharedPreference.SharedPreference
import com.example.weatherpaint.databinding.MainWeatherFragmentBinding
import com.example.weatherpaint.repository.retrofit.Day
import com.example.weatherpaint.repository.retrofit.Hourly
import com.example.weatherpaint.repository.retrofit.MainWeather
import com.example.weatherpaint.repository.retrofit.Wind
import com.example.weatherpaint.ui.activity.MainActivity
import com.example.weatherpaint.ui.fragments.other.CityFragment
import com.example.weatherpaint.ui.fragments.other.Const.C
import com.example.weatherpaint.ui.fragments.other.Const.CHIP_D
import com.example.weatherpaint.ui.fragments.other.Const.CHIP_N
import com.example.weatherpaint.ui.fragments.other.Const.CLEAR
import com.example.weatherpaint.ui.fragments.other.Const.CLOUDS
import com.example.weatherpaint.ui.fragments.other.Const.F
import com.example.weatherpaint.ui.fragments.other.Const.FORMAT
import com.example.weatherpaint.ui.fragments.other.Const.KEY_CITY
import com.example.weatherpaint.ui.fragments.other.Const.RAIN
import com.example.weatherpaint.ui.fragments.other.Const.SNOW
import com.example.weatherpaint.ui.fragments.other.Const.START_CITY
import com.example.weatherpaint.ui.fragments.other.Const.START_LAT
import com.example.weatherpaint.ui.fragments.other.Const.START_LON
import com.example.weatherpaint.ui.fragments.other.Const.TEMP_C
import com.example.weatherpaint.ui.fragments.other.Const.convertUnix
import com.example.weatherpaint.ui.fragments.other.Const.toCelsius
import com.example.weatherpaint.viewModel.StateApp
import com.example.weatherpaint.viewModel.ViewModelMainWeatherFragment
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class MainWeatherFragment : Fragment() , MainWeatherInterface {
    private var _binding : MainWeatherFragmentBinding? = null
    private val binding  : MainWeatherFragmentBinding get() = _binding!!

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val dayDao: DaoDB by inject { parametersOf(requireActivity()) }

    private var lat = START_LAT
    private var lon = START_LON
    private var cityName = START_CITY

    private var weather = CLEAR

    override fun setLatLon(lat: String, lon: String, cityName: String){
        this.lat = lat
        this.lon = lon
        this.cityName = cityName

        SharedPreference.saveDataLocation("$cityName;$lat;$lon",KEY_CITY,requireActivity() as MainActivity)
        viewModel.sendServerWeather(this.lat, this.lon)
    }

    private val viewModel: ViewModelMainWeatherFragment by lazy {
        ViewModelProvider(this).get(ViewModelMainWeatherFragment::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = MainWeatherFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { it -> render(it) })

        checkButton()
        searchCityDrop()
        setCityFragment()
        createViewWeatherAnimation()

        loadDataOptions()

        viewModel.sendServerWeather(lat,lon)
    }

    private fun render(data : StateApp){
        when(data){
            is StateApp.Loading -> {

                viewHide(View.VISIBLE)
            }
            is StateApp.Success -> {

                data.weather.main?.temp?.let { setTemp(it) }
                data.weather.main?.let { setMainWeather(it) }
                data.weather.wind?.let { setWindWeather(it) }
                data.weather.weather?.get(0)?.weatherDescription?.let { setWeatherDescription(it) }
            }
            is StateApp.SuccessOneCall -> {

                data.weather.listHourly?.let   { setGraphTemp(it) }
                data.weather.days?.get(0)?.let { creteWeatherDayList(it) }

                viewHide(View.GONE)
                binding.temperatureGraph.setStart(true,1.2f,true)
            }
            is StateApp.SuccessOneCallToAdd -> {
                data.weather.days?.get(0)?.let { addDayDB(it) }
            }
            is StateApp.Error -> {
                Toast.makeText(requireContext(),getString(R.string.error),Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadDataOptions() {
        val data = SharedPreference.loadDataCity(KEY_CITY,requireActivity() as MainActivity)

        data?.let { val str = data.split(";")
            cityName = str[0]
            lat = str[1]
            lon = str[2]
        }
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun creteWeatherDayList(day: Day) = with(binding){
        val listWeather = listOf(getString(R.string.mor),getString(R.string.day_ru),getString(R.string.eve_ru),getString(R.string.night_ru))
        setHeaderDate(day.unixDT)

        when(day.weather[0].weather){
            CLEAR  -> { weatherImage.load(R.drawable.w_sun)    }
            CLOUDS -> { weatherImage.load(R.drawable.w_cloudy) }
            SNOW   -> { weatherImage.load(R.drawable.w_snow)   }
            RAIN   -> { weatherImage.load(R.drawable.w_rainy)  }
        }

        textMor.text = listWeather[0]
        textTempMor.text = String.format("%.1f",toCelsius(day.tempDay.morn)) + C

        textDay.text = listWeather[1]
        textTempDay.text = String.format("%.1f",toCelsius(day.tempDay.day)) + C

        textEve.text = listWeather[2]
        textTempEve.text = String.format("%.1f",toCelsius(day.tempDay.eve)) + C

        textNight.text = listWeather[3]
        textTempNight.text = String.format("%.1f",toCelsius(day.tempDay.night)) + C

        textHeaderDayWeather.text = day.weather[0].weather
    }

    private fun createViewWeatherAnimation(){
        val currentDate = Date()
        val timeFormat = SimpleDateFormat("HH", Locale.getDefault())
        val time = timeFormat.format(currentDate).toInt()

        if ( (17..23).contains(time) || (0..5).contains(time) ) {
            binding.weatherAnimation.setDay(false)
        } else {
            binding.weatherAnimation.setDay(true)
        }

        val valueDay = SharedPreference.loadDataBoolean(CHIP_D,requireActivity() as MainActivity)
        if(valueDay){
            binding.weatherAnimation.setDay(true)
        }
        val valueNight = SharedPreference.loadDataBoolean(CHIP_N,requireActivity() as MainActivity)
        if(valueNight){
            binding.weatherAnimation.setDay(false)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setHeaderDate(unix: String){

        val list = getString(R.string.seasons).split(",")
        binding.textHeaderDay.text = "${convertUnix(unix,"dd").toInt()} ${list[convertUnix(unix,"MM").toInt() - 1]}"
    }

    private fun viewHide(visibility : Int) = with(binding) {
        loading.visibility = visibility
    }

    data class HourlyTemp(var temp: Float, val date : String)

    @SuppressLint("SetTextI18n")
    private fun setGraphTemp(listHourly: List<Hourly>){

        val list = ArrayList<HourlyTemp>(16)

        for (i in 0 until (listHourly.size / 4)) {
            list.add( HourlyTemp( toCelsius(listHourly[i * 2].temp) , convertUnix(listHourly[i * 2].unixDT,"HH")) )
        }
        binding.temperatureGraph.upDate(list)
    }

    @SuppressLint("SetTextI18n")
    private fun setTemp(temp : String){
        val isC = SharedPreference.loadDataBooleanDefTrue(TEMP_C, requireActivity() as MainActivity)

        if(isC) {
            binding.temp.text  = "${toCelsius(temp).roundToInt()}" + C
        }
        else {
            binding.temp.text =  "${(9 * ( temp.toFloat() - 273.15f )/5 + 32).roundToInt()}" + F
        }
    }

    private fun setWeatherDescription(weather : String){
        binding.weatherDescription.text = weather
        this.weather = weather

        setWeatherAnimation(weather)
    }

    @SuppressLint("SetTextI18n")
    private fun setMainWeather(weather: MainWeather) = with(binding){
        humidity.text = getString(R.string.humidity) + weather.humidity + getString(R.string.persent)
        pressure.text = getString(R.string.pressure) + weather.pressure + getString(R.string.press)
        temp2.text    = getString(R.string.feels_like) + String.format("%.1f",toCelsius(weather.feels_like)) + C

        local.text = cityName
    }

    @SuppressLint("SetTextI18n")
    private fun setWindWeather(weather: Wind){
        binding.speedWind.text = getString(R.string.wind) + weather.speedWind + getString(R.string.speed)
    }

    private fun searchCityDrop(){
        binding.cardSearch.setOnClickListener {

            val constraint = ConstraintSet()
            constraint.clone(requireContext(), R.layout.main_weather_fragment)

            constraint.connect(R.id.containerTowns,ConstraintSet.START, R.id.constraintLayout, ConstraintSet.START)
            constraint.connect(R.id.containerTowns,ConstraintSet.END,   R.id.constraintLayout, ConstraintSet.END)

            animationStart(constraint)
        }
    }

    override fun cityFragmentClose(){
        val constraint = ConstraintSet()
        constraint.clone(requireContext(), R.layout.main_weather_fragment)

        animationStart(constraint)
    }

    private fun animationStart(constraint: ConstraintSet,timeAnimation : Long = 1500, startDelay : Long = 0) {
        val transition = ChangeBounds()
        transition.duration     = timeAnimation
        transition.interpolator = AnticipateOvershootInterpolator(1f)
        transition.startDelay   = startDelay

        TransitionManager.beginDelayedTransition(binding.constraintLayout, transition)
        constraint.applyTo(binding.constraintLayout)
    }

    private fun setCityFragment() {
        val cityFragment = CityFragment.newInstance()
        cityFragment.setMainFragment(this)

        childFragmentManager
            .beginTransaction()
            .replace(R.id.containerTowns,cityFragment)
            .commit()
    }

    private fun setWeatherAnimation(weather: String) = with(binding){
        textMode.text = getString(R.string.real_weather)

        switchWeather.isChecked = false
        switchWeather2.isChecked = false
        switchWeather3.isChecked = false

        when(weather){
            CLEAR -> { weatherAnimation.setWeatherSimulation (CLEAR, 99000,99000) }
            CLOUDS -> { weatherAnimation.setWeatherSimulation(CLOUDS,4000, 99000) }
            SNOW  -> { weatherAnimation.setWeatherSimulation (SNOW,  4000, 40) }
            RAIN  -> { weatherAnimation.setWeatherSimulation (RAIN,  4000, 10) }
        }
    }

    private fun checkButton() = with(binding){
        textMode.text = getString(R.string.real_weather)

        switchWeather.setOnClickListener {
            switchWeather2.isChecked = false
            switchWeather3.isChecked = false
            textMode.text = getString(R.string.test_mode)

            weatherAnimation.setWeatherSimulation(SNOW, 4000, 40)
            if(!switchWeather.isChecked) setWeatherAnimation(weather)
        }
        switchWeather2.setOnClickListener {
            switchWeather.isChecked = false
            switchWeather3.isChecked = false
            textMode.text = getString(R.string.test_mode)

            weatherAnimation.setWeatherSimulation(RAIN, 4000, 10)

            if(!switchWeather2.isChecked) setWeatherAnimation(weather)
        }
        switchWeather3.setOnClickListener {
            switchWeather.isChecked = false
            switchWeather2.isChecked = false
            textMode.text = getString(R.string.test_mode)

            weatherAnimation.setWeatherSimulation(CLEAR, 90000, 90000)
            if(!switchWeather3.isChecked) setWeatherAnimation(weather)
        }

        chip.setOnClickListener {
            viewModel.sendServerWeather(lat, lon)
            upDate()
        }

        addWeatherDB()
    }

    private var rotateIs = true
    private fun upDate(){
        var rotation = -360f * 2f

        if (!rotateIs){
            rotation = 0f
            rotateIs = true
        } else {
            rotateIs = false
        }

        val anim = ObjectAnimator.ofFloat(binding.imageViewLoading2,View.ROTATION,rotation)
        anim.duration     = 2000
        anim.interpolator = AnticipateOvershootInterpolator(1f)
        anim.start()
    }

    private fun addDayDB(day: Day){

        scope.launch {
            dayDao.insertDay(
                EntitiesWeatherDay(
                    mornT = day.tempDay.morn,
                    dayT = day.tempDay.day,
                    eveT = day.tempDay.eve,
                    nightT = day.tempDay.night,

                    unixDT = day.unixDT,
                    humidity = day.humidity,
                    wind_speed = day.wind_speed,
                    pressure = day.pressure,

                    weather = day.weather[0].weather,
                    date = convertUnix(day.unixDT, FORMAT),
                    city = cityName
                )
            )
        }
    }

    private fun addWeatherDB() {
        binding.chipAddDay.setOnClickListener {
            Toast.makeText(requireContext(),getString(R.string.addNotesWeather),Toast.LENGTH_SHORT).show()
            viewModel.sendServerWeatherToAdd(lat, lon)
        }
    }

    companion object {
        fun newInstance() = MainWeatherFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        _binding = null
    }
}