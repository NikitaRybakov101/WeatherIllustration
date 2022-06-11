package com.example.weatherpaint.ui.splashScreen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import com.example.weatherpaint.R
import com.example.weatherpaint.ui.activity.MainActivity
import kotlinx.coroutines.*
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@SuppressLint("ViewConstructor")
class CanvasAnimation(context: Context, private val mainActivity: MainActivity) : View(context) {

    private var heightPix = 2220f
    private var margin = -50

    private val paint = Paint().apply {

        val displayMetrics = DisplayMetrics()
        (context as MainActivity).windowManager.getDefaultDisplay().getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        heightPix = displayMetrics.heightPixels.toFloat()

        if(width > 900){
            textSize = 80f
            margin = -50
        } else {
            textSize = 50f
            margin = -30
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!

        canvas.drawColor(Color.rgb(255, 255, 255))

        paint.color = Color.rgb(18, 28, 54)
        paint.strokeWidth = 4f

        animationLine1(canvas)
        animationLine2(canvas)

        textAnimation(canvas)
        lineAnimation(canvas)

        invalidate()
    }
    private var i = 0.0
    private var j = 0.0

    private fun textAnimation(canvas: Canvas){
        paint.color = Color.rgb(18, 28, 54)

        val string = context.getString(R.string.weather_heder_anim)
        val array  =  string.toCharArray()
        canvas.drawText(array,0, i.toInt() ,(width/4).toFloat(),(height/2 - height/7).toFloat(),paint)

        if (i < string.length){
            i += 0.15
        } else {
            val string2 = context.getString(R.string.illustration_header)
            val array2  =  string2.toCharArray()
            canvas.drawText(array2,0, j.toInt() ,(width/4).toFloat(),(height/2 - height/10).toFloat(),paint)

            if (j < string2.length) {
                j += 0.15
            } else {
                mainActivity.setFragment()
            }
        }
    }

    private var saveX = 0

    private var flag = true
    private var endX = 0.0

    private var flag2 = true
    private var endX2 = 0.0

    private var vector = 0.0

    private var endY1 = 0.0
    private var endY2 = 0.0

    private fun animationLine1(canvas: Canvas){
        if (flag){
            endX = (width/2 + width/3).toDouble()
            flag = false
        }
        val startX = (width/2  + width/3)  .toFloat()
        val startY = (height/2 - height/16).toFloat()

        if(endX > (width/2 - width/(3.5))) {
            endX -= 12
        } else {
            val x = (endX   + vector * cos(-5 * PI/4)).toFloat()
            val y = (startY + vector * sin(-5 * PI/4)).toFloat()

            if(vector < height/20){
                vector += 2
            } else {
                saveX = x.toInt()
                if(endY1 < abs(y - (height/2 + height/16))){
                    endY1 += 5
                }
                canvas.drawLine(x, y, x , y + endY1.toFloat() , paint)
            }
            canvas.drawLine(endX.toFloat(), startY, x , y , paint)
        }
        canvas.drawLine(startX, startY, endX.toFloat(), startY, paint)
    }

    private fun animationLine2(canvas: Canvas){
        if (flag2){
            endX2 = (width/2 - width/3).toDouble()
            flag2 = false
        }
        val startX2 = (width/2  - width/3).toFloat()
        val startY2 = (height/2 + height/16).toFloat()

        if(endX2 < (width/2 + width/(3.5))) {
            endX2 += 12
        } else {
            val x = (endX2   + vector * cos(-PI/4)).toFloat()
            val y = (startY2 + vector * sin(-PI/4)).toFloat()

            if(vector < height/20){
                vector += 2
            } else {
                if(endY2 < abs(y - (height/2 - height/16))){
                    endY2 += 5
                }
                canvas.drawLine(x, y, x ,y - endY2.toFloat() , paint)
            }
            canvas.drawLine(endX2.toFloat(), startY2, x , y , paint)
        }
        canvas.drawLine(startX2, startY2, endX2.toFloat(), startY2, paint)
    }
    private var y = 1
    private var end = 10.0

    private fun lineAnimation(canvas: Canvas){
        paint.color = Color.rgb(18, 28, 54)
        paint.strokeWidth = 3f

        y = 0
        while (y <= end) {
            canvas.drawLine((width / 4 - width / 38).toFloat(), (margin + y + (height / 2 - height / 7)).toFloat(),
                            (width / 4 - width / 10).toFloat(), (margin + y + (height / 2 - height / 7)).toFloat(), paint)
            y += (heightPix/111f).toInt()
        }
        if(end < (heightPix/16f).toInt()) {
            end += 1.5
        }
    }
}