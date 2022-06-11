package com.example.weatherpaint.ui.fragments.other

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.weatherpaint.R
import com.example.weatherpaint.databinding.FragmentCityBinding
import com.example.weatherpaint.ui.fragments.main.MainWeatherFragment
import com.example.weatherpaint.ui.fragments.other.Const.FILE_NAME_CITY

class CityFragment : Fragment() {
    private var _binding : FragmentCityBinding? = null
    private val binding  : FragmentCityBinding get() = _binding!!

    private lateinit var mainFragment : MainWeatherFragment
    private val arrayListCity = ArrayList<String>()

    data class Coordinates(val lat: String,val lon: String)
    private val arrayCoordinates = ArrayList<Coordinates>()

    private val fileName = FILE_NAME_CITY

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCityBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDropList()
        setBackPress()
    }

    private fun setBackPress() = with(binding){
        rightSlide.setOnClickListener {
            mainFragment.cityFragmentClose()
        }

        cityApply.setOnClickListener {
            val index = arrayListCity.indexOf( textEditLayout.text.toString() )

            if (index >= 0 ) {
                mainFragment.setLatLon(arrayCoordinates[index].lat, arrayCoordinates[index].lon, arrayListCity[index])
                mainFragment.cityFragmentClose()
            } else {
                textEditLayout.error = getString(R.string.error_ru)
            }
        }
    }

    private fun setDropList(){
        requireContext().assets.open(fileName)
            .bufferedReader()
            .readLines()
            .forEach { string ->
            val str = string.replace(" ", "").split(";")[0].split(",")

            arrayListCity.add( str[0] )
            arrayCoordinates.add( Coordinates(str[1].replace("\t","") , str[2].replace("\t","")) )
        }

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_list_sity, arrayListCity)
        binding.textEditLayout.setAdapter(arrayAdapter)
    }

    fun setMainFragment(fragment: MainWeatherFragment){
        mainFragment = fragment
    }

    companion object {
        fun newInstance() = CityFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}