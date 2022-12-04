package com.example.assignment2

import android.graphics.Point
import java.util.ArrayList

class ArimaaGame(private val arimaaDelegate: ArimaaDelegate) {

    private lateinit var currentPiece: ArimaaPiece
    val arimaaBoard: ArimaaBoard = ArimaaBoard()
    private var GoldPlayer = true
    private var numberOfMove = 4
    var fromRow = 0
    var fromCol = 0
    var isSelected = false
    private var endGame = false
    private var checkBeforePush = false
    private val boardStates: MutableList<BoardState?> = ArrayList()


    init {
        boardStates.add(arimaaBoard.State())
    }

    private val highlightPoints: List<Point>
    private get() = highlightPoint(fromRow, fromCol)

    private fun highlightPoint(fromRow: Int, fromCol: Int): List<Point> {
        val highlightPoints: MutableList<Point> = ArrayList()
        if (fromCol != 0 && !endGame)
            highlightPoints.add(Point(fromRow, fromCol - 1))
        if (fromCol != 7 && !endGame)
            highlightPoints.add(Point(fromRow, fromCol + 1))
        if (fromRow != 0 && !endGame)
            highlightPoints.add(Point(fromRow - 1, fromCol))
        if (fromRow != 7 && !endGame)
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

        if (checkBeforePush) {checkBeforePush(toRow, toCol)
            return }

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
                arimaaDelegate.onError("You cannot move opponent piece.")
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
                if (moveError == null && !endGame) {
                    moveArimaaPiece(fromRow, fromCol, toRow, toCol)
                    numberOfMove--
                    if (arimaaPiece.goldenPlayer != GoldPlayer) {
                        val push = Push(arimaaPiece)
                        if (push.size == 1) {
                            val Push = push[0]
                            moveArimaaPiece(Push.x, Push.y, fromRow, fromCol)
                            numberOfMove--
                        }
                        else {
                            checkBeforePush = true
                            arimaaDelegate.showMessage("Select your piece to use for pushing.")
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


        if(endGame) {
            arimaaDelegate.onError("Game Over, Reset The Game or Quit ")
        }
    }


    private fun moveArimaaPiece(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int) {
        val from = getArimaaPiece(fromRow, fromCol) ?: return
        val to = getArimaaPiece(toRow, toCol)
        if (to != null) return
        setArimaaPiece(fromRow, fromCol, null)
        setArimaaPiece(toRow, toCol, from)
        TrapPosition()
        if(EndGame()) endGame=true

    }

        private fun TrapPosition() {
        val trapSequares = arrayOf(
            Point(2, 2), Point(5, 2), Point(2, 5),
            Point(5, 5)
        )
        for (point in trapSequares) {
            if (Trap(point)) {
                val arimaaPiece = getArimaaPiece(point.x, point.y)
                setArimaaPiece(point.x, point.y, null)
                arimaaBoard.TrapPosition().add(arimaaPiece)
            }
        }
    }

    private fun Trap(position: Point): Boolean {
        val arimaaPiece = getArimaaPiece(position.x, position.y) ?: return false
        for (legalMove in highlightPoint(position.x, position.y)) {
            val neighbor = getArimaaPiece(legalMove.x, legalMove.y) ?: continue
            if (arimaaPiece.goldenPlayer == neighbor.goldenPlayer) return false
        }
        return true
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
                && highlight.rank > currentPiece.rank
            ) {
                push.add(point)
            }
        }
        return push
    }

    private fun checkBeforePush(Row: Int, Col: Int) {
        val arimaaPiece = getArimaaPiece(Row, Col)
        if (arimaaPiece == null) {
            arimaaDelegate.onError("Please select a piece to use for pushing.")
        } else {
            val push = Push(currentPiece)
            if (!push.contains(Point(Row, Col))) {
                arimaaDelegate.onError("You cannot push using this piece.")
            } else {
                moveArimaaPiece(Row,Col,fromRow,fromCol)
                numberOfMove--
                checkBeforePush = false
                isSelected = false
                arimaaDelegate.updateBoard()
            }
        }
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

    private fun EndGame(): Boolean {
        for (row in 0 until 8) {
            var arimaaPiece = getArimaaPiece(row, 0)
            if (arimaaPiece != null && arimaaPiece.pieceType == PieceType.Rabbit &&
                !arimaaPiece.goldenPlayer
            ) {

                arimaaDelegate.endGame(false)
                return true
            }
            arimaaPiece = getArimaaPiece(row, 7)
            if (arimaaPiece != null && arimaaPiece.pieceType == PieceType.Rabbit &&
                arimaaPiece.goldenPlayer
            ) {

                arimaaDelegate.endGame(true)
                return true
            }
        }
        var trapGrabbit = 0
        var trapSrabbit = 0
        for (arimaaPiece in arimaaBoard.TrapPosition()) {
            if (arimaaPiece?.pieceType == PieceType.Rabbit) {
                if (arimaaPiece.goldenPlayer) trapGrabbit++ else trapSrabbit++
            }
        }
        if (GoldPlayer) {
            if (trapSrabbit == 8) {
                arimaaDelegate.endGame(true)
                return true
            }
        }
        if (trapGrabbit == 8) {
            arimaaDelegate.endGame(false)
            return true
        }
        if (trapSrabbit == 8) {
            arimaaDelegate.endGame(true)
            return true
        }
        return false
    }


}