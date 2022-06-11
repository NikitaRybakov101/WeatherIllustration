package com.example.weatherpaint.ui.fragments.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.weatherpaint.R
import com.example.weatherpaint.databinding.ContainerNavigationFragmentBinding
import com.example.weatherpaint.ui.fragments.other.InfoFragment
import com.example.weatherpaint.ui.fragments.other.OptionsFragment
import com.example.weatherpaint.ui.fragments.favoriteFragment.FavorFragment
import com.example.weatherpaint.ui.fragments.other.Const.STACK
import com.example.weatherpaint.ui.fragments.weekWeatherFragment.WeekWeatherFragment

class MainContainerFragment : Fragment() {
    private var _binding : ContainerNavigationFragmentBinding? = null
    private val binding  : ContainerNavigationFragmentBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ContainerNavigationFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toggle = ActionBarDrawerToggle(requireActivity(), binding.drawer, binding.toolbar, R.string.open, R.string.close)
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()

        setFirstFragment()
        bottomNav()
        navigation()
        toolbar()
    }

    private fun setFirstFragment(){
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, MainWeatherFragment.newInstance())
            .commit()
    }

    private fun bottomNav(){
        binding.bottomNavigationView.setOnItemSelectedListener{
            when(it.itemId){
                R.id.homeWeather  -> { setFragment(MainWeatherFragment.newInstance()) }
                R.id.weekWeather ->  { setFragment(WeekWeatherFragment.newInstance()) }
                R.id.favoriteWeather -> { setFragment(FavorFragment.newInstance()) }
            }
            true
        }
    }

    private fun toolbar(){
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.toolbar_setting -> { setFragmentOptions(OptionsFragment.newInstance()) }
                R.id.toolbar_info    -> { setFragment(InfoFragment.newInstance())    }
            }
            true
        }
    }

    private fun navigation() {
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeWeather -> {
                    setFragment(MainWeatherFragment.newInstance())
                    binding.drawer.closeDrawer(GravityCompat.START)
                }
                R.id.weekWeather -> {
                    setFragment(WeekWeatherFragment.newInstance())
                    binding.drawer.closeDrawer(GravityCompat.START)
                }
                R.id.favoriteWeather -> {
                    setFragment(FavorFragment.newInstance())
                    binding.drawer.closeDrawer(GravityCompat.START)
                }
                R.id.toolbar_setting -> {
                    setFragmentOptions(OptionsFragment.newInstance())
                    binding.drawer.closeDrawer(GravityCompat.START)
                }
                R.id.info -> {
                    setFragment(InfoFragment.newInstance())
                    binding.drawer.closeDrawer(GravityCompat.START)
                }
            }
            false
        }
    }

    private fun setFragment(fragment: Fragment){
        requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.anim_layout_2,R.anim.anim_layout)
            .replace(R.id.fragmentContainer,fragment)
            .commit()
    }

    private fun setFragmentOptions(fragment: Fragment){
        requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.anim_layout_2,R.anim.anim_layout)
            .addToBackStack(STACK)
            .replace(R.id.fragmentContainer,fragment)
            .commit()
    }

    companion object {
        fun newInstance() = MainContainerFragment()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}