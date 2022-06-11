package com.example.weatherpaint.ui.splashScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.weatherpaint.ui.activity.MainActivity

class SplashFragment : Fragment() {
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return CanvasAnimation(requireContext(),mainActivity)
    }

    companion object {
        fun newInstance(mainActivity: MainActivity) : SplashFragment {
            val fragment = SplashFragment()
            fragment.mainActivity = mainActivity
            return fragment
        }
    }
}