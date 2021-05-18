package com.example.android_chess

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ChessView(context: Context?, attrs: AttributeSet?): View(context, attrs) {
    private final val startX = 20f
    private final val startY = 180f
    private final val squareSize = 130f

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        val color = Paint()
        color.color = Color.argb(255, 238, 238, 212)
        canvas?.drawRect(startX, startY, startX + 8 * squareSize, startY + 8 * squareSize, color)
        color.color = Color.argb(255, 125, 148, 93)
        for (i in 0..3) {
            for (j in 0..3) {
                canvas?.drawRect(startX + squareSize + 2 * j * squareSize, startY + 2 * i * squareSize, startX + 2 * squareSize + 2 * j * squareSize, startY + squareSize + 2 * i * squareSize, color)
                canvas?.drawRect(startX + 2 * j * squareSize, startY + squareSize + 2 * i * squareSize, startX + squareSize + 2 * j * squareSize, startY + 2 * squareSize + 2 * i * squareSize, color)
            }
        }
    }
}