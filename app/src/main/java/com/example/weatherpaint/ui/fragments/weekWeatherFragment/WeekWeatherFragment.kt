package com.example.weatherpaint.ui.fragments.weekWeatherFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherpaint.R
import com.example.weatherpaint.data.sharedPreference.SharedPreference
import com.example.weatherpaint.databinding.WeekWeatherFragmentBinding
import com.example.weatherpaint.repository.retrofit.Day
import com.example.weatherpaint.ui.activity.MainActivity
import com.example.weatherpaint.ui.fragments.other.Const
import com.example.weatherpaint.ui.fragments.other.Const.KEY_CITY
import com.example.weatherpaint.ui.fragments.recyclerWeek.RecyclerWeekAdapter
import com.example.weatherpaint.viewModel.StateApp
import com.example.weatherpaint.viewModel.ViewModelWeekFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeekWeatherFragment : Fragment() , InterfaceWeekFragment{
    private var _binding : WeekWeatherFragmentBinding? = null
    private val binding : WeekWeatherFragmentBinding get() =  _binding!!

    private val viewModel: ViewModelWeekFragment by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = WeekWeatherFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { it -> render(it) })
        sendServer()
    }

    private fun sendServer(){
        val data = SharedPreference.loadDataCity(KEY_CITY,requireActivity() as MainActivity)

        data?.let { val str = data.split(";")
            val lat = str[1]
            val lon = str[2]

            viewModel.sendServer(lat, lon)
        }
    }

    private fun render(data : StateApp){
        when(data){
            is StateApp.Loading -> {
                binding.progressWeekWeather.visibility = View.VISIBLE
            }
            is StateApp.SuccessOneCall -> {
                binding.progressWeekWeather.visibility = View.GONE
                data.weather.days?.let { processingOneCall(it) }
            }
            is StateApp.Error ->   {
                Toast.makeText(requireContext(),getString(R.string.error),Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    override fun callBackRecycler(day: Day) {
        val dayDetails = DataDetailsWeather(
            day.tempDay.morn,
            day.tempDay.day,
            day.tempDay.eve,
            day.tempDay.night,

            day.unixDT,day.humidity,day.wind_speed,day.pressure,day.weather[0].weather)

        val fragment = DetailsWeekFragment.getDetailsFragment(dayDetails)

        requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.anim_layout_2,R.anim.anim_layout)
            .replace(R.id.cardContainer,fragment)
            .commit()
    }

    private fun processingOneCall(days : List<Day>){
        val list = getString(R.string.seasonsHeader).split(",")
        binding.recyclerHeader.text =  list[Const.convertUnix(days[0].unixDT, "MM").toInt() - 1]

        setRecycler(days)
    }

    private fun setRecycler(listDay : List<Day>) = with(binding){
        recycler.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        recycler.adapter = RecyclerWeekAdapter(listDay,this@WeekWeatherFragment)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = WeekWeatherFragment()
    }
}