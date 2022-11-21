package com.example.assignment2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class CustomView (context: Context?) : View(context) {

    private val scale = 1.0f
    private var widthX = 20f
    private var highetY = 200f
    private var sequareSize = 130f
    private val lightBrown = Color.parseColor("#997950")
    private val darkBrown = Color.parseColor("#7C4700")
    private val paint = Paint()
    private var _context: Context? = context
    private var _attribs: AttributeSet? = null


    constructor(context: Context?, attribs: AttributeSet?) : this(context) {
        _attribs = attribs
    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val smaller = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(smaller, smaller)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        val ArimaaBoard = min(width, height) * scale
        sequareSize = ArimaaBoard / 8f
        widthX = (width - ArimaaBoard) / 2f
        highetY = (height - ArimaaBoard) / 2f

        drawArimaaBoard(canvas!!)
    }


    private fun drawArimaaBoard(canvas: Canvas) {
        for (row in 0 until 8)
            for (col in 0 until 8)


                if(row ==2 && col ==2)
                    drawSquareAt(canvas,2,2,true)
                    else if (row == 2 && col ==5 )
                    drawSquareAt(canvas,2,5,true)
                    else if (row == 5 && col ==2 )
                    drawSquareAt(canvas,5,2,true)
                    else if (row ==5 && col ==5)
                    drawSquareAt(canvas,5,5,true)

               else drawSquareAt(canvas, col, row, false)
    }

    private fun drawSquareAt(canvas: Canvas, col: Int, row: Int, isTrap: Boolean) {
        paint.color = if (isTrap) darkBrown else lightBrown
        paint.setStyle(Paint.Style.FILL)

        canvas.drawRect(widthX + col * sequareSize, highetY + row * sequareSize, widthX + (col + 1)* sequareSize, highetY + (row + 1) * sequareSize, paint)
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3f)
        canvas.drawRect(widthX + col * sequareSize, highetY + row * sequareSize, widthX + (col + 1)* sequareSize, highetY + (row + 1) * sequareSize, paint);
    }


}