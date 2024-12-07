package com.cs407.dummybudget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.util.Log
import kotlin.math.cos
import kotlin.math.sin

class BudgetView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val outerCirclePaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
        isAntiAlias = true
    }

    private val innerCirclePaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
        isAntiAlias = true
    }

    private val spokePaint = Paint().apply {
        color = Color.DKGRAY
        strokeWidth = 5f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        Log.d("BudgetView", "onDraw called")

        val centerX = width / 2f
        val centerY = height / 2f - 600f
        val outerRadius = (width.coerceAtMost(height)) / 3f
        val innerRadius = outerRadius / 1.5f

        // Draw outer circle
        canvas.drawCircle(centerX, centerY, outerRadius, outerCirclePaint)

        // Draw inner circle (no color, just the background)
        canvas.drawCircle(centerX, centerY, innerRadius, innerCirclePaint)

        // Draw spokes from outer circle to inner circle
        val numSpokes = 6
        for (i in 0 until numSpokes) {
            val angle = Math.toRadians((360.0 / numSpokes) * i)

            // Calculate the coordinates where the spoke starts on the outer circle
            val xOuter = (centerX + outerRadius * cos(angle)).toFloat()
            val yOuter = (centerY + outerRadius * sin(angle)).toFloat()

            // Calculate the coordinates where the spoke ends on the inner circle
            val xInner = (centerX + innerRadius * cos(angle)).toFloat()
            val yInner = (centerY + innerRadius * sin(angle)).toFloat()

            // Draw the spoke from the outer circle to the inner circle
            canvas.drawLine(xOuter, yOuter, xInner, yInner, spokePaint)
        }
    }
}
