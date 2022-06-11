package com.example.weatherpaint.ui.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import com.example.weatherpaint.ui.activity.MainActivity
import com.example.weatherpaint.ui.fragments.main.MainWeatherFragment
import kotlin.math.abs

class TemperatureGraph @JvmOverloads constructor(context : Context, attrs : AttributeSet? = null, style: Int = 0) : View(context,attrs,style) {

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        val displayMetrics = DisplayMetrics()
        (context as MainActivity).windowManager.getDefaultDisplay().getMetrics(displayMetrics)
        val widthScreen = displayMetrics.widthPixels

        if(widthScreen < 900) {
            height = (height / 2f).toInt()
        }
        setMeasuredDimension(width, height)
    }
    private var start : Boolean = false
    private var listTemp : List<MainWeatherFragment.HourlyTemp> = listOf()
    private var speed : Float = 0.7f
    private var flagText = true

    fun setStart(start : Boolean,speed: Float,flag : Boolean){
        this.start = start
        this.length = 0
        this.speed = speed
        this.flagText = flag
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!

        if(start) {
            createView(listTemp)
            setLineAnimation(canvas)
            graphWeather(canvas)
        }
        invalidate()
    }

    private val paint = Paint().apply {
        strokeWidth = 10f
        color = Color.rgb(199, 199, 199)
    }

    private val paintPost = Paint().apply {
        color = Color.rgb(192, 204, 207)
    }

    private val paintText = Paint().apply {

        val displayMetrics = DisplayMetrics()
        (getContext() as MainActivity).windowManager.getDefaultDisplay().getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        if(width > 900){
            textSize = 36f
            margin = 10
        } else {
            textSize = 22f
            margin = -5
        }
        color = Color.rgb(99, 106, 107)
    }

    private var length : Int = 0
    private fun setLineAnimation(canvas: Canvas){

        canvas.drawLine(0f, height.toFloat() - width / 26, length.toFloat(), height.toFloat() - width / 26,paint)
        if (length < width) length += width / 41
    }

    private var listPost = ArrayList<Post>(16)
    private var rad     = 0f
    private var padding = 0f
    private var pad     = 0f
    private val const   = 2.4f
    private var margin  = 10

    class Post(val maxH: Float, var h: Float, val posX: Int, var speed: Float,var temp : String,val date : String)

    private fun graphWeather(canvas: Canvas){
        listPost.forEach { e -> if(e.h < e.maxH) e.h += e.speed }
        listPost.forEach { e ->
            canvas.drawRoundRect(RectF(e.posX.toFloat() - rad,
                height - e.h - rad - margin,
                e.posX + rad,
                height - rad - margin * 2 + 5),
                46f,46f,paintPost)

            canvas.drawText("${(e.temp)}°",e.posX.toFloat() - margin * 3f,height.toFloat() - e.h - rad - margin * 3,paintText)

            if (flagText){
                canvas.drawText("${(e.date)}ч",e.posX.toFloat() - margin * 3f,   height.toFloat(),paintText)
            } else {
                canvas.drawText("${(e.date)} ",e.posX.toFloat() - margin * 3f,   height.toFloat(),paintText)
            }
        }
    }

    private var flag = true
    private fun createView(listTemp : List<MainWeatherFragment.HourlyTemp>) {
        if(flag) {
            padding = (width / (2f * listTemp.size * const + listTemp.size - 1f))
            rad = padding * const ; pad = rad

            val minE = minElement(listTemp)
            if(minE < 0){
                listTemp.forEach { e -> e.temp += abs(minE) + 1 }
            }
            val consScale = height * 0.5f / maxElement(listTemp)

            for (i in listTemp.indices) {
                val speed = listTemp[i].temp / ((listTemp[0].temp  / (listTemp[0].temp  / speed)))
                listPost.add(Post(listTemp[i].temp * consScale + 2 * margin, 0f, pad.toInt(), abs(speed), format(i,minE), listTemp[i].date))
                pad += padding + 2 * rad
            }
            flag = false
        }
    }

    private fun format(i : Int,minE : Float) : String{
        return if(minE < 0) {
            String.format("%.1f", listTemp[i].temp - abs(minE) - 1)
        } else {
            String.format("%.1f", listTemp[i].temp)
        }
    }

    private fun minElement(listTemp : List<MainWeatherFragment.HourlyTemp>) : Float{
        var min = 0f
        for (i in listTemp.indices){
            if(listTemp[i].temp < min){
                min = listTemp[i].temp
            }
        }
        return min
    }

    private fun maxElement(listTemp : List<MainWeatherFragment.HourlyTemp>) : Float{
        var max = 0f
        for (i in listTemp.indices){
            if(listTemp[i].temp > max){
                max = listTemp[i].temp
            }
        }
        return max
    }

    fun upDate(listTemp : List<MainWeatherFragment.HourlyTemp>){
        flag = true
        listPost.clear()
        this.listTemp = listTemp
    }
}