package com.example.weatherpaint.ui.customView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import com.example.weatherpaint.R
import com.example.weatherpaint.ui.activity.MainActivity
import com.example.weatherpaint.ui.fragments.other.Const.RAIN
import com.example.weatherpaint.ui.fragments.other.Const.SNOW
import java.util.*
import kotlin.collections.ArrayList

class WeatherAnimation @JvmOverloads constructor(context : Context, attrs : AttributeSet? = null, style: Int = 0) : View(context,attrs,style){


    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(widthMeasureSpec)

        setMeasuredDimension(width, height/2)
    }

    private val bitmapSourceBg:    Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sky)
    private val bitmapBg:          Bitmap = Bitmap.createBitmap(bitmapSourceBg, 0, 0,  bitmapSourceBg.width ,bitmapSourceBg.height)

    private val bitmapSourceBgNight: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sky_night_low_res)
    private val bitmapBgNight:       Bitmap = Bitmap.createBitmap(bitmapSourceBgNight, 0, 0,  bitmapSourceBgNight.width ,bitmapSourceBgNight.height)

    private val bitmapSource2:     Bitmap = BitmapFactory.decodeResource(resources, R.drawable.cloud)
    private val bitmapCloudWhite1: Bitmap = Bitmap.createBitmap(bitmapSource2, 0, 0,  bitmapSource2.width ,bitmapSource2.height)

    private val bitmapSourceMoon:  Bitmap = BitmapFactory.decodeResource(resources, R.drawable.moon_low_res)
    private val bitmapMoon:        Bitmap = Bitmap.createBitmap(bitmapSourceMoon, 0, 0,  bitmapSourceMoon.width ,bitmapSourceMoon.height)

    private val bitmapSourceSun:   Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sun)
    private val bitmapSun:         Bitmap = Bitmap.createBitmap(bitmapSourceSun, 0, 0,  bitmapSourceSun.width ,bitmapSourceSun.height)

    private val bitmapSourceSnow:  Bitmap = BitmapFactory.decodeResource(resources, R.drawable.snow)
    private val bitmapSnow:        Bitmap = Bitmap.createBitmap(bitmapSourceSnow, 0, 0,  bitmapSourceSnow.width ,bitmapSourceSnow.height)

    private val bitmapSourceDrop:  Bitmap = BitmapFactory.decodeResource(resources, R.drawable.drop_water2)
    private val bitmapDrop:        Bitmap = Bitmap.createBitmap(bitmapSourceDrop, 0, 0,  bitmapSourceDrop.width ,bitmapSourceDrop.height)

    private val paint = Paint().apply {
        strokeWidth = 2f
        Color.WHITE
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!

        createImageBg(canvas)
        createImageSunMoon(canvas)

        animationFallout(canvas)

        generatedCloud()
        createFallout()

        cloudAnimation(canvas)

        deleteCloud()
        deleteFallout()

        invalidate()
    }

    private var isDay = true
    fun setDay(isDay : Boolean){
        this.isDay = isDay
    }

    private fun createImageSunMoon(canvas: Canvas){
        if(!isDay) {
            val ratio = (bitmapSourceMoon.height.toFloat() / bitmapSourceMoon.width.toFloat())
            val w = width / 5.5
            val h = (ratio * w).toInt()

            canvas.drawBitmap(bitmapMoon, null, Rect((width / 5), 0 + 25, ((width / 5) + w).toInt(), h + 25), paint)
        } else {
            val ratio = (bitmapSourceSun.height.toFloat() / bitmapSourceSun.width.toFloat())
            val w = width / 5.5
            val h = (ratio * w).toInt()

            canvas.drawBitmap(bitmapSun, null, Rect((width / 5), 0 + 25, ((width / 5) + w).toInt(), h + 25), paint)
        }
    }

    private fun createImageBg(canvas: Canvas){
        if(isDay) {
            val ratio = (bitmapSourceBg.height.toFloat() / bitmapSourceBg.width.toFloat())
            val w = width
            val h = (ratio * w).toInt()

            canvas.drawBitmap(bitmapBg, null, Rect(0, (height - h), w, height), paint)
        } else {
            val ratio = (bitmapSourceBgNight.height.toFloat() / bitmapSourceBgNight.width.toFloat())
            val w = width
            val h = (ratio * w).toInt()

            canvas.drawBitmap(bitmapBgNight, null, Rect(0, (height - h), w, height), paint)
        }
    }

    private val listCloud = ArrayList<Cloud>(16)
    private val random = Random()

    class Cloud  (var posX: Float, val posY: Int, val speed : Float, val posZ : Int, val bitmap: Bitmap,val src: Bitmap)

    class Fallout(var posX: Float, var posY: Float, val type: String,  val speedY: Float, val posZ: Int,
                  val speedX: Float, val scale: Float, val bitmap: Bitmap,val src: Bitmap, val speedHorizontal: Int,val scale2 : Float)

    private fun cloudAnimation(canvas: Canvas){

        listCloud.forEach { e -> e.posX += e.speed
            val ratio = (e.src.height.toFloat() / e.src.width.toFloat())
            var w = width / 6.4f
            var h = (ratio * w).toInt()

            if(e.posZ == 0) {
                canvas.drawBitmap(e.bitmap, null, Rect(e.posX.toInt(), e.posY + 70, (w + e.posX).toInt(), h + e.posY + 70), paint)
            } else {
                w = (width / 5).toFloat()
                h = (ratio * w).toInt()
                canvas.drawBitmap(e.bitmap, null, Rect(e.posX.toInt(), e.posY + 120, (w + e.posX).toInt(), h + e.posY + 120), paint)
            }
        }
    }

    private val listFallout = ArrayList<Fallout>()
    private var timeTemp = Date().time
    private var timeTempCloud = Date().time

    private var fallout = SNOW

    private var intensityFallout = 40
    private var intensityCloud = 4000

    fun setWeatherSimulation(fallout: String, intensityCloud: Int, intensityFallout: Int){
        this.fallout = fallout
        this.intensityCloud = intensityCloud
        this.intensityFallout = intensityFallout
    }

    private fun createFallout(){

        val date = Date()
        if (date.time - timeTemp > intensityFallout) {

            if(listCloud.size > 0) {
                val index = (0 until listCloud.size).random()
                val cloud = listCloud[index]

                when (fallout) {
                    SNOW -> {
                        listFallout.add(Fallout(
                                cloud.posX + random.nextInt(90), cloud.posY + 10f,
                                SNOW, 2f + random.nextInt(2), cloud.posZ, cloud.speed,
                                22f, bitmapSnow, bitmapSourceSnow, 1, 30f))
                    }
                    RAIN -> {
                        listFallout.add(Fallout(
                                cloud.posX + random.nextInt(90), cloud.posY + 10f,
                                RAIN, 15f + random.nextInt(3), cloud.posZ, cloud.speed,
                                70f, bitmapDrop, bitmapSourceDrop, 7, 100f))
                    }
                }
            }
            timeTemp = date.time
        }
    }

    private fun animationFallout(canvas: Canvas){
        listFallout.forEach { e -> e.posX += e.speedX - e.speedHorizontal
                                   e.posY += e.speedY

            val ratio = (e.src.height.toFloat() / e.src.width.toFloat())
            var w = width / e.scale
            var h = (ratio * w).toInt()

            if(e.posZ == 0) {
                w = width / e.scale2
                h = (ratio * w).toInt()

                canvas.drawBitmap(e.bitmap, null, Rect(e.posX.toInt(), (e.posY + 70f).toInt(),  (w + e.posX).toInt(), (h + e.posY + 70).toInt()),  paint)
            } else {
                canvas.drawBitmap(e.bitmap, null, Rect(e.posX.toInt(), (e.posY + 120f).toInt(), (w + e.posX).toInt(), (h + e.posY + 120).toInt()), paint)
            }
        }
    }

    private fun generatedCloud(){
        val date = Date()
        if (date.time - timeTempCloud > random.nextInt(intensityCloud) + (intensityCloud/2)) {

            val y = random.nextInt(50)
            val speedRandom = (0.5 / (random.nextInt(5) + 1)).toFloat()

            if(random.nextInt(2) == 0) {
                listCloud.add(Cloud(-150f, y,0.5f + speedRandom, 0,bitmapCloudWhite1,bitmapSource2))
            } else {
                listCloud.add(Cloud(-150f, y,1.2f + speedRandom, 1,bitmapCloudWhite1,bitmapSource2))
            }
            timeTempCloud = date.time
        }
    }

    private fun deleteCloud(){
        for (i in 0 until listCloud.size) {
            if (listCloud[i].posX > width) {
                listCloud.remove(listCloud[i])
                return
            }
        }
    }

    private fun deleteFallout(){
        for (i in 0 until listFallout.size) {
            if (listFallout[i].posY > height) {
                listFallout.remove(listFallout[i])
                return
            }
        }
    }
}