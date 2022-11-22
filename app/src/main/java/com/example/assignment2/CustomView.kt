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

    private val bitmaps = mutableMapOf<Int, Bitmap>()
    private val imgIDs = setOf(
        R.drawable.gold_camel,
        R.drawable.gold_elephant,
        R.drawable.gold_cat,
        R.drawable.gold_dog,
        R.drawable.gold_horse,
        R.drawable.gold_rabbit,
        R.drawable.silver_camel,
        R.drawable.silver_elephant,
        R.drawable.silver_cat,
        R.drawable.silver_dog,
        R.drawable.silver_horse,
        R.drawable.silver_rabbit,)

    init {
        loadBitmaps()
    }


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
        drawPieces(canvas)
    }


    private fun drawPieces(canvas: Canvas) {
        val arimaaGame = ArimaaGame
        arimaaGame.reset()
        for (row in 0 until 8) {
            for (col in 0 until 8) {
                val piece = arimaaGame.pieceAt(col, row)
                if (piece != null) {
                    drawPieceAt(canvas, col, row, piece.resID)
                }

            }
        }}

    private fun drawPieceAt(canvas: Canvas, col: Int, row: Int, resID: Int) =
        canvas.drawBitmap(bitmaps[resID]!!, null, RectF(widthX + col * sequareSize,highetY + (7 - row) * sequareSize,widthX + (col + 1) * sequareSize,highetY + ((7 - row) + 1) * sequareSize), paint)
    private fun loadBitmaps() =
        imgIDs.forEach { imgID ->
            bitmaps[imgID] = BitmapFactory.decodeResource(resources, imgID)
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