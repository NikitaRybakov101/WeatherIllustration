package com.example.weatherpaint.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.telephony.CarrierConfigManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.weatherpaint.R
import com.example.weatherpaint.data.sharedPreference.SharedPreference
import com.example.weatherpaint.ui.fragments.main.MainContainerFragment
import com.example.weatherpaint.ui.fragments.other.Const.FIRST
import com.example.weatherpaint.ui.fragments.other.Const.NIGHT_THEME
import com.example.weatherpaint.ui.splashScreen.SplashFragment

class MainActivity : AppCompatActivity() , InterfaceMainActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadTheme()
        setContentView(R.layout.activity_main)

        if(SharedPreference.loadDataBooleanDefTrue(FIRST,this)){
            setSplashScreen()
        } else {
            SharedPreference.saveDataBoolean(true, FIRST,this)
            setFragment()
        }
    }

    override fun setFragment(){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.containerActivity, MainContainerFragment.newInstance())
            .commit()
    }

    private fun setSplashScreen() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.containerActivity,SplashFragment.newInstance(this))
            .commit()
    }

    private fun loadTheme(){
        val op = SharedPreference.loadDataBoolean(NIGHT_THEME,this)
        if(op){
            setTheme(R.style.DarkTheme)
        }else{
            setTheme(R.style.LightTheme)
        }
    }
}