package com.example.weatherpaint.ui.fragments.other

import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.weatherpaint.R
import com.example.weatherpaint.data.sharedPreference.SharedPreference
import com.example.weatherpaint.databinding.OptionsFragmentBinding
import com.example.weatherpaint.gpsGetLocal.LocalListener
import com.example.weatherpaint.ui.activity.MainActivity
import com.example.weatherpaint.ui.fragments.main.MainWeatherFragment
import com.example.weatherpaint.ui.fragments.other.Const.CHIP_D
import com.example.weatherpaint.ui.fragments.other.Const.CHIP_N
import com.example.weatherpaint.ui.fragments.other.Const.FIRST
import com.example.weatherpaint.ui.fragments.other.Const.NIGHT_THEME
import com.example.weatherpaint.ui.fragments.other.Const.TEMP_C

class OptionsFragment : Fragment() {
    private var _binding : OptionsFragmentBinding? = null
    private val binding  : OptionsFragmentBinding get() = _binding!!

    private lateinit var locationManager : LocationManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = OptionsFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadOptions()
        checkButton()
    }

    private fun loadOptions() = with(binding){
        chipC.isChecked = getPreferenceChipTemp(TEMP_C)
        chipF.isChecked = !getPreferenceChipTemp(TEMP_C)
        switchNight.isChecked = getPreference(NIGHT_THEME)

        chipD.isChecked = getPreference(CHIP_D)
        chipN.isChecked = getPreference(CHIP_N)
    }
    private fun getPreference(key: String) = SharedPreference.loadDataBoolean(key,requireActivity() as MainActivity)
    private fun getPreferenceChipTemp(key: String) = SharedPreference.loadDataBooleanDefTrue(key,requireActivity() as MainActivity)
    private fun setPreference(boolean: Boolean,key: String) = SharedPreference.saveDataBoolean(boolean,key,requireActivity() as MainActivity)

    private fun checkButton() = with(binding){
        cardInfo.setOnClickListener {
            setFragment(InfoFragment.newInstance())
        }

        switchNight.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setPreference(true,NIGHT_THEME)
                setPreference(false, FIRST)

                activity?.setTheme(R.style.DarkTheme)
                activity?.recreate()

            } else {
                setPreference(false,NIGHT_THEME)
                setPreference(false, FIRST)

                activity?.setTheme(R.style.LightTheme)
                activity?.recreate()
            }
        }

        chipC.setOnClickListener {
            if (chipC.isChecked) {

                chipF.isChecked = false
                setPreference(true,TEMP_C)
            } else {
                chipF.isChecked = true
                setPreference(false,TEMP_C)
            }
        }
        chipF.setOnClickListener {
            if (chipF.isChecked) {

                chipC.isChecked = false
                setPreference(false,TEMP_C)
            } else {
                chipC.isChecked = true
                setPreference(true,TEMP_C)
            }
        }

        chipD.setOnClickListener {
            if (chipD.isChecked) {

                chipN.isChecked = false
                setPreference(true,CHIP_D)
                setPreference(false,CHIP_N)
            } else {
                setPreference(false,CHIP_D)
            }
        }
        chipN.setOnClickListener {
            if (chipN.isChecked) {

                chipD.isChecked = false
                setPreference(true, CHIP_N)
                setPreference(false, CHIP_D)
            } else {
                setPreference(false, CHIP_N)
            }
        }

        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        buttonMaterialAuto.setOnClickListener {
            checkPermissions()
        }

        arrowBack.setOnClickListener {
            setFragment(MainWeatherFragment.newInstance())
        }
    }

    private fun checkPermissions() {
        if( ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION), 100)
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100,1000f,LocalListener(requireActivity()))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == 100 && grantResults.size == 2) {
            if( (grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED) ){
                checkPermissions()
            } else {
                Toast.makeText(requireContext(),getString(R.string.no_permissions),Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setFragment(fragment: Fragment){
        requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.anim_layout_2,R.anim.anim_layout)
            .replace(R.id.fragmentContainer,fragment)
            .commit()
    }


    companion object {
        fun newInstance() = OptionsFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}