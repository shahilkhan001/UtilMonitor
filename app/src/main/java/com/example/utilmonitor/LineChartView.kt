package com.example.utilmonitor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class LineChartView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val linePaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 5f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val pointPaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val usageData = mutableListOf<Int>()

    fun setData(data: List<Int>) {
        usageData.clear()
        usageData.addAll(data)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (usageData.size < 2) return

        val stepX = width.toFloat() / (usageData.size - 1)
        val maxY = usageData.maxOrNull() ?: 1
        val minY = usageData.minOrNull() ?: 0

        for (i in 0 until usageData.size - 1) {
            val x1 = i * stepX
            val y1 = height - (usageData[i] - minY) * height.toFloat() / (maxY - minY + 1)
            val x2 = (i + 1) * stepX
            val y2 = height - (usageData[i + 1] - minY) * height.toFloat() / (maxY - minY + 1)
            canvas.drawLine(x1, y1, x2, y2, linePaint)
            canvas.drawCircle(x1, y1, 8f, pointPaint)
        }
        // Draw last point
        val lastX = (usageData.size - 1) * stepX
        val lastY = height - (usageData.last() - minY) * height.toFloat() / (maxY - minY + 1)
        canvas.drawCircle(lastX, lastY, 8f, pointPaint)
    }
}
