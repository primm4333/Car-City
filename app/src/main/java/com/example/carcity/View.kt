//View.kt

package com.example.carcity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class View(var c: Context, var gameTask: Task) : View(c) {
    private var myPaint: Paint? = null
    private var speed = 1
    private var time = 0
    private var score = 0
    private var highScore = 0
    private var myCarPosition = 0
    private val enemyCars = ArrayList<HashMap<String, Any>>() // Store multiple enemy cars

    var viewWidth = 0
    var viewHeight = 0

    init {
        myPaint = Paint()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if (time % 700 < 10 + speed) {
            // Add new enemy cars
            val map = HashMap<String, Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            map["color"] = if ((0..1).random() == 0) "blue" else "red" // Randomly choose blue or red
            enemyCars.add(map)
        }

        time = time + 10 + speed
        val carWidth = viewWidth / 5
        val carHeight = carWidth + 10
        myPaint!!.style = Paint.Style.FILL

        // Draw the white car
        val playerCarDrawable = resources.getDrawable(R.drawable.whitec, null)
        playerCarDrawable.setBounds(
            myCarPosition * viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight - 2 - carHeight,
            myCarPosition * viewWidth / 3 + viewWidth / 15 + carWidth - 25,
            viewHeight - 2
        )
        playerCarDrawable.draw(canvas)
        myPaint!!.color = Color.GREEN

        // Draw enemy cars
        for (i in enemyCars.indices) {
            try {
                var carX = enemyCars[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                var carY = time - enemyCars[i]["startTime"] as Int

                val color = enemyCars[i]["color"] as String
                val drawableId = if (color == "blue") R.drawable.bluec else R.drawable.red
                val enemyCarDrawable = resources.getDrawable(drawableId, null)
                enemyCarDrawable.setBounds(
                    carX + 25, carY - carHeight, carX + carWidth - 25, carY
                )
                enemyCarDrawable.draw(canvas)

                // Check collision
                if (enemyCars[i]["lane"] as Int == myCarPosition) {
                    if (carY > viewHeight - 2 - carHeight && carY < viewHeight - 2) {
                        gameTask.closeGame(score, highScore)
                    }
                }

                // Remove passed enemy cars + counting score
                if (carY > viewHeight + carHeight) {
                    enemyCars.removeAt(i)
                    score++
                     speed = 1 + Math.abs(score / 5)
                    if (score > highScore) {
                        highScore = score
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //score and speed text
        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score : $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed : $speed", 380f, 80f, myPaint!!)

        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                val x1 = event.x
                if (x1 < viewWidth / 2) {
                    if (myCarPosition > 0) {
                        myCarPosition--
                    }
                }
                if (x1 > viewWidth / 2) {
                    if (myCarPosition < 2) {
                        myCarPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
            }
        }

        return true
    }
}
