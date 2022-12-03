package com.example.assignment2

import android.graphics.Point
import java.util.ArrayList

class ArimaaGame(private val arimaaDelegate: ArimaaDelegate) {

    private lateinit var currentPiece: ArimaaPiece
    val arimaaBoard: ArimaaBoard = ArimaaBoard()
    private var GoldPlayer = true
    var numberOfMove = 4
    var fromRow = 0
    var fromCol = 0
    var isSelected = false
    private val boardStates: MutableList<BoardState?> = ArrayList()



    private val highlightPoints: List<Point>
    private get() = highlightPoint(fromRow, fromCol)

    private fun highlightPoint(fromRow: Int, fromCol: Int): List<Point> {
        val highlightPoints: MutableList<Point> = ArrayList()
        if (fromCol != 0)
            highlightPoints.add(Point(fromRow, fromCol - 1))
        if (fromCol != 7)
            highlightPoints.add(Point(fromRow, fromCol + 1))
        if (fromRow != 0)
            highlightPoints.add(Point(fromRow - 1, fromCol))
        if (fromRow != 7)
            highlightPoints.add(Point(fromRow + 1, fromCol))
        return highlightPoints
    }



    val legalmoves: List<Point>
        get() {
         val arimaaPiece = getArimaaPiece(fromRow, fromCol)
         return legalMove(arimaaPiece, fromRow, fromCol)
        }


    fun legalMove(arimaaPiece: ArimaaPiece?, fromRow: Int, fromCol: Int): List<Point> {
        val legalmoves: MutableList<Point> = ArrayList()
        for (point in highlightPoints) {
            val toPoint = getArimaaPiece(point.x, point.y)
            if (toPoint != null && toPoint !== arimaaPiece) continue
            val error = arimaaPiece!!.ErrorMessages(
                fromRow, fromCol, point.x, point.y,
                GoldPlayer
            )
            if (error == null)
                legalmoves.add(point)
        }
        return legalmoves
    }

    fun onPieceClick(toRow: Int, toCol: Int) {
        var arimaaPiece = getArimaaPiece(toRow,toCol)

        if (!isSelected) {
            if (arimaaPiece == null) return
            if (numberOfMove == 0) {
                arimaaDelegate.onError("All moves used. Either finish the turn or undo move.")
                return finishTurn()
            }
            fromRow = toRow
            fromCol = toCol
            val goldenPiece = arimaaPiece.goldenPlayer == GoldPlayer
            if (!goldenPiece && (numberOfMove < 2 || Push(arimaaPiece).isEmpty())) {
                arimaaDelegate.onError("You cannot move this piece. It's not your turn")
                return
            }
            if (goldenPiece && Immobile(arimaaPiece, fromRow, fromCol)) {
                arimaaDelegate.onError("The selected piece is immobile.")
                return
           }

            val allowedmoves = legalmoves
            if (allowedmoves.isNotEmpty()) {
                isSelected = true
                currentPiece = arimaaPiece
                arimaaDelegate.updateBoard()
            } else {
                arimaaDelegate.onError("No possible moves are available for selected piece.")
            }
        }
        else {
            if (arimaaPiece == null) {
                arimaaPiece = getArimaaPiece(fromRow, fromCol)
                val moveError = arimaaPiece!!.ErrorMessages(
                    fromRow, fromCol, toRow, toCol,
                    GoldPlayer
                )
                if (moveError == null) {
                    moveArimaaPiece(fromRow, fromCol, toRow, toCol)
                    numberOfMove--
                    if (arimaaPiece.goldenPlayer != GoldPlayer) {
                        val push = Push(arimaaPiece)
                        if (push.size == 1) {
                            val push = push[0]
                            moveArimaaPiece(push.x, push.y, fromRow, fromCol)
                            numberOfMove--
                        }
                    }
//                     else if (numberOfMove > 0) {
//                        val pull = Pull(arimaaPiece)
//                        if (pull.size > 0) {
//                            arimaaDelegate.notifyPull()
//                        }
//                    }
                } else arimaaDelegate.onError(moveError)
            } else {
                if (toRow != fromRow || toCol != fromCol) {
                    arimaaDelegate.onError("Another piece exists there.")
                }
            }
            isSelected = false
            arimaaDelegate.updateBoard()
        }
    }


    private fun moveArimaaPiece(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int) {
        val srcPiece = getArimaaPiece(fromRow, fromCol) ?: return
        val destPiece = getArimaaPiece(toRow, toCol)
        if (destPiece != null) return
        setArimaaPiece(fromRow, fromCol, null)
        setArimaaPiece(toRow, toCol, srcPiece)

    }


    private fun Immobile(currentPiece: ArimaaPiece, Row: Int, Col: Int): Boolean {
        var canMove = false
        for (point in highlightPoint(Row, Col)) {
            val nearPiece = getArimaaPiece(point.x, point.y) ?: continue
            if (currentPiece.goldenPlayer == nearPiece.goldenPlayer) return false
            if (nearPiece.rank > currentPiece.rank) canMove = true

        }
        return canMove
    }

    fun Push(currentPiece: ArimaaPiece?): List<Point> {
        val push: MutableList<Point> = ArrayList()
        if (legalMove(currentPiece, fromRow, fromCol).isEmpty()) {
            return push
        }
        for (point in highlightPoints) {
            val highlight = getArimaaPiece(point.x, point.y) ?: continue
            if (Immobile(highlight, point.x, point.y)) continue
            if (highlight.goldenPlayer != currentPiece!!.goldenPlayer

            ) {
                push.add(point)
            }
        }
        return push
    }

//    fun Pull(currentPiece: ArimaaPiece?): List<Point> {
//        val pull: MutableList<Point> = ArrayList()
//        for (point in highlightPoints) {
//            val neighbor = getArimaaPiece(point.x, point.y) ?: continue
//            if (neighbor.goldenPlayer != currentPiece!!.goldenPlayer
//
//            ) {
//                pull.add(point)
//            }
//        }
//        return pull
//    }


    private fun getArimaaPiece(row: Int, col: Int): ArimaaPiece? {
        return arimaaBoard.getArimaaPieces()[row]!![col]
    }

    private fun setArimaaPiece( row: Int, col: Int , arimaaPiece: ArimaaPiece?) {
        arimaaBoard.getArimaaPieces()[row]!![col] = arimaaPiece
    }


    fun finishTurn() {
        val currentState = arimaaBoard.State()
        val lastState = boardStates[boardStates.size - 1]
        if (currentState == lastState) {
            numberOfMove = 4
            arimaaDelegate.onError("You have not made any changes.")
            return
        }
        for (oldState in boardStates) {
            if (currentState == oldState) {
                arimaaDelegate.onError("This state has been seen before!, make a move or undo")
                return
            }
        }
        boardStates.add(currentState)
        GoldPlayer = !GoldPlayer
        numberOfMove = 4

        isSelected = false
        arimaaDelegate.endTurn(GoldPlayer)
        arimaaDelegate.updateBoard()
    }

    init {
        boardStates.add(arimaaBoard.State())
    }
}