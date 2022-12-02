package com.example.assignment2

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.min

class CustomView (context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val scale = 1.0f
    private var widthX = 20f
    private var highetY = 200f
    private var sequareSize = 130f
    private val paint = Paint()
    private val lightBrown = Color.parseColor("#997950")
    private val darkBrown = Color.parseColor("#7C4700")
    private lateinit var _blue: Paint
    private lateinit var _green: Paint

    private var _width = 0
    private var fromRow = 0
    private var fromCol = 0
    private var _context: Context? = context
    private var _attribs: AttributeSet? = null
    var arimaaGame: ArimaaGame? = null
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
        _blue = Paint(Paint.ANTI_ALIAS_FLAG)
        _blue.setColor(Color.argb(240, 33, 76, 249))
        _green = Paint(Paint.ANTI_ALIAS_FLAG)
        _green.setColor(Color.argb(240, 33, 249, 98))
        loadBitmaps()
    }

    fun Start(arimmaGame:ArimaaGame?) {
        this.arimaaGame = arimmaGame
        invalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val smaller = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(smaller, smaller)
        _width = measuredWidth
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        val ArimaaBoard = min(width, height) * scale
        sequareSize = ArimaaBoard / 8f
        widthX = (width - ArimaaBoard) / 2f
        highetY = (height - ArimaaBoard) / 2f

        drawArimaaBoard(canvas!!)
        drawHighlightPoints(canvas!!)
        drawPieces(canvas!!)
    }


        override fun onTouchEvent(event: MotionEvent): Boolean {
            if (arimaaGame == null) return super.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                fromRow = Row(event.x)
                fromCol = Col(event.y)
                return true
                }

            MotionEvent.ACTION_UP -> {
                val toRow = Row(event.x)
                val toCol = Col(event.y)
                if (toRow == fromRow && toCol == fromCol) arimaaGame!!.onPieceClick(toRow, toCol)
                return true
            }
            }
            return super.onTouchEvent(event)
    }

    private fun drawHighlightPoints(canvas: Canvas) {
        if (arimaaGame != null && arimaaGame!!.isSelected) {
            val fromRow = arimaaGame!!.fromRow
            val fromCol = 7 - arimaaGame!!.fromCol
            canvas.drawRect(
                fromRow * sequareSize, fromCol * sequareSize,
                (fromRow + 1) * sequareSize, (fromCol + 1) * sequareSize, _blue!!
            )
            val allowedMoves = arimaaGame!!.legalmoves
            for (point in allowedMoves) {
                val toRow = point.x
                val toCol = 7 - point.y
                canvas.drawRect(
                    toRow * sequareSize, toCol * sequareSize,
                    (toRow + 1) * sequareSize, (toCol + 1) * sequareSize, _green!!
                )
            }

        }
    }

    private fun drawPieces(canvas: Canvas) {
        if (arimaaGame ==null) return
        for (row in 0 until 8) {
            for (col in 0 until 8) {

                val arimaaPiece = arimaaGame!!.arimaaBoard.getArimaaPieces()[row]?.get(col) ?: continue

                    drawPieceAt(canvas,row,col,arimaaPiece.resID)
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

private fun Row(touchX: Float): Int {
    return (touchX / _width * 8).toInt()
}

private fun Col(touchY: Float): Int {
    return 7 - (touchY / _width * 8).toInt()
}


}