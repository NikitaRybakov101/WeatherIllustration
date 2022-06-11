package com.example.weatherpaint.ui.fragments.favoriteFragment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import coil.load
import com.example.weatherpaint.R
import com.example.weatherpaint.data.dataBase.EntitiesWeatherDay
import com.example.weatherpaint.databinding.ViewDbRecyclerBinding
import com.example.weatherpaint.ui.fragments.other.Const
import kotlin.math.round

class RecyclerAddedFragment(private val fragment: FavorFragment, private val listDays: ArrayList<EntitiesWeatherDay>, private val recyclerView: RecyclerView) : RecyclerView.Adapter<RecyclerAddedFragment.ViewHolderAdded>(){
    private var _binding : ViewDbRecyclerBinding? = null
    private val binding : ViewDbRecyclerBinding get() =  _binding!!

    private lateinit var cardWeekTemp : CardView

    inner class ViewHolderAdded(view : View) : RecyclerView.ViewHolder(view) {
        val textCelsius = binding.textCelsius
        val textDate = binding.textDate
        val imageWeather = binding.imageWeather
        val cityName = binding.textCity
        val cardWeek = binding.cardWeek
        val cardAnim = binding.cardAnim

        val textTempMor = binding.textTempMor
        val textTempDay = binding.textTempDay
        val textTempEve = binding.textTempEve
        val textTempNight = binding.textTempNight

        val humidity = binding.humidity
        val pressure = binding.pressure
        val speedWind = binding.speedWind
        val temp2 = binding.temp2

        private val deleteButton = binding.deleteButton

        fun itemRemoved(){
            deleteButton.setOnClickListener {
                fragment.deleteDayDataBase(listDays[layoutPosition].id)

                listDays.removeAt(layoutPosition)
                notifyItemRemoved(layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAddedFragment.ViewHolderAdded {
        _binding = ViewDbRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolderAdded(binding.root)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAddedFragment.ViewHolderAdded, position: Int) {
        holder.textCelsius.text = round(Const.toCelsius(listDays[position].mornT)).toInt().toString() + " C°"

        holder.textDate.text = listDays[position].date
        holder.cityName.text = listDays[position].city

        when(listDays[position].weather){
            Const.CLEAR  -> { holder.imageWeather.load(R.drawable.w_sun)    }
            Const.CLOUDS -> { holder.imageWeather.load(R.drawable.w_cloudy) }
            Const.SNOW   -> { holder.imageWeather.load(R.drawable.w_snow)   }
            Const.RAIN   -> { holder.imageWeather.load(R.drawable.w_rainy)  }
        }
        animWeekCard(holder,position)
        holder.itemRemoved()
        setDataDetails(listDays[position],holder)
    }
    override fun getItemCount() = listDays.size

    private fun animWeekCard(holder: RecyclerAddedFragment.ViewHolderAdded, position: Int){
        val density = holder.cardAnim.context.resources.displayMetrics.density

        if (position == 0) {
            animation()
            val layout = holder.cardAnim.layoutParams
            layout.height = (158 * density).toInt()
            holder.cardAnim.layoutParams = layout
        }
        holder.cardWeek.setOnClickListener {
            cardWeekTemp = holder.cardAnim
            animation()
            if(cardWeekTemp.layoutParams.height ==  (158 * density).toInt()) {
                val layout = cardWeekTemp.layoutParams
                layout.height = 1
                cardWeekTemp.layoutParams = layout
            } else {
                val layout = cardWeekTemp.layoutParams
                layout.height = (158 * density).toInt()
                cardWeekTemp.layoutParams = layout
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setDataDetails(day: EntitiesWeatherDay, holder: ViewHolderAdded) = with(holder) {
        textTempMor.text   = "${String.format("%.1f", Const.toCelsius(day.mornT)) }${Const.C}"
        textTempDay.text   = "${String.format("%.1f", Const.toCelsius(day.dayT))  }${Const.C}"
        textTempEve.text   = "${String.format("%.1f", Const.toCelsius(day.eveT))  }${Const.C}"
        textTempNight.text = "${String.format("%.1f", Const.toCelsius(day.nightT))}${Const.C}"

        humidity.text  = fragment.getString(R.string.humidity) +day.humidity + fragment.getString(R.string.persent)
        pressure.text  = fragment.getString(R.string.pressure) +day.pressure + fragment.getString(R.string.press)
        speedWind.text = fragment.getString(R.string.wind) +day.wind_speed + fragment.getString(R.string.speed)
        temp2.text     = fragment.getString(R.string.feels_like) +String.format("%.1f", Const.toCelsius(day.dayT)) + Const.C
    }

    private fun animation(){
        val transition = ChangeBounds()
        transition.duration     = 500
        transition.interpolator = AnticipateOvershootInterpolator(0f)
        TransitionManager.beginDelayedTransition(recyclerView, transition)
    }
}