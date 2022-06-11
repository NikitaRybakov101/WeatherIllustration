package com.example.weatherpaint.ui.fragments.recyclerWeek

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weatherpaint.R
import com.example.weatherpaint.databinding.ViewRecyclerBinding
import com.example.weatherpaint.repository.retrofit.Day
import com.example.weatherpaint.ui.fragments.other.Const.C
import com.example.weatherpaint.ui.fragments.other.Const.CLEAR
import com.example.weatherpaint.ui.fragments.other.Const.CLOUDS
import com.example.weatherpaint.ui.fragments.other.Const.RAIN
import com.example.weatherpaint.ui.fragments.other.Const.SNOW
import com.example.weatherpaint.ui.fragments.other.Const.convertUnix
import com.example.weatherpaint.ui.fragments.other.Const.toCelsius
import com.example.weatherpaint.ui.fragments.weekWeatherFragment.WeekWeatherFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class RecyclerWeekAdapter(private val listDays : List<Day>,private val fragment : WeekWeatherFragment) : RecyclerView.Adapter<RecyclerWeekAdapter.WeekViewHolder>() {
    private var _binding : ViewRecyclerBinding? = null
    private val binding : ViewRecyclerBinding get() =  _binding!!
    private lateinit var cardWeekTemp : CardView
    private lateinit var cardWeekTemp2 : CardView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerWeekAdapter.WeekViewHolder {
        _binding = ViewRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WeekViewHolder(binding.root)
    }

    inner class WeekViewHolder(layout : View) : RecyclerView.ViewHolder(layout) {
        val textCelsius = binding.textCelsius
        val textDate = binding.textDate
        val textMonth = binding.month
        val imageWeather = binding.imageWeather
        val cardWeek = binding.cardWeek
        val cardAnim = binding.cardAnim
    }

    override fun getItemCount() = listDays.size

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        _binding = null
    }

    override fun onBindViewHolder(holder: RecyclerWeekAdapter.WeekViewHolder, position: Int) {
        dataCard(holder, position)
        animWeekCard(holder,position)
    }

    @SuppressLint("SetTextI18n")
    private fun dataCard(holder: WeekViewHolder, position: Int){
        holder.textCelsius.text = round(toCelsius(listDays[position].tempDay.day)).toInt().toString() + " C°"

        val unix = listDays[position].unixDT
        val list = fragment.getString(R.string.seasons).split(",")
        holder.textDate.text  = "${convertUnix(unix,"dd").toInt()}"
        holder.textMonth.text =  list[convertUnix(unix, "MM").toInt() - 1]

        when(listDays[position].weather[0].weather){
            CLEAR  -> { holder.imageWeather.load(R.drawable.w_sun)    }
            CLOUDS -> { holder.imageWeather.load(R.drawable.w_cloudy) }
            SNOW   -> { holder.imageWeather.load(R.drawable.w_snow)   }
            RAIN   -> { holder.imageWeather.load(R.drawable.w_rainy)  }
        }
    }

    private fun animWeekCard(holder: WeekViewHolder,position: Int){
        if(position == 0) {
            objectAnimator(holder.cardWeek,50f)
            objectAnimator(holder.cardAnim,300f)

            cardWeekTemp = holder.cardAnim
            cardWeekTemp2 = holder.cardWeek
            cardWeekTemp2.alpha = 0.8f

            fragment.callBackRecycler(listDays[position])
        }
        holder.cardWeek.setOnClickListener {
            objectAnimator(cardWeekTemp2,0f)
            objectAnimator(cardWeekTemp, 0f)
            cardWeekTemp2.alpha = 1f
            cardWeekTemp.alpha = 1f

            objectAnimator(holder.cardWeek,50f)
            objectAnimator(holder.cardAnim,300f)

            cardWeekTemp = holder.cardAnim
            cardWeekTemp2 = holder.cardWeek
            cardWeekTemp2.alpha = 0.8f

            fragment.callBackRecycler(listDays[position])
        }
    }

    private fun objectAnimator(cardView: CardView, x : Float) {
        val animator = ObjectAnimator.ofFloat(cardView,View.X,x)
        animator.duration = 500
        animator.start()
    }
}