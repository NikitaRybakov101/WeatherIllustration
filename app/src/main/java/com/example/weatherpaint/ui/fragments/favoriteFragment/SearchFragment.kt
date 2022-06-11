package com.example.weatherpaint.ui.fragments.favoriteFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import com.example.weatherpaint.databinding.SearchFragmentBinding

class SearchFragment : Fragment() {
    private var _binding : SearchFragmentBinding? = null
    private val binding : SearchFragmentBinding get() = _binding!!

    private var fragment : FavorFragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = SearchFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chekButton()
    }

    private fun chekButton() = with(binding){

        buttonApply.setOnClickListener {
            val date = editText.text.toString()
            val city = textEditLayout.text.toString()

            fragment?.searchDataBase(city,date)
        }

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            var monthString = month.toString()
            var dayString = dayOfMonth.toString()

            if (month + 1 < 10)  { monthString = "0${month + 1}" }
            if (dayOfMonth < 10) { dayString = "0$dayOfMonth"    }
            val selectedDate = "$year-${monthString}-$dayString"

            val charArray = CharArray(selectedDate.length)
            var i = 0
            selectedDate.forEach { e -> charArray[i] = e
                i++
            }
            editText.setText(charArray,0,selectedDate.length)
        }
    }

    companion object {
        fun newInstance(fragment: FavorFragment) : SearchFragment {
            val searchFragment = SearchFragment()
            searchFragment.setFragment(fragment)

            return searchFragment
        }
    }

    fun setFragment(fragment: FavorFragment){
        this.fragment = fragment
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}