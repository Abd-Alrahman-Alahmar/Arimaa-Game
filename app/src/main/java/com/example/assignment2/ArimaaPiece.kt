package com.example.assignment2

import kotlin.math.abs

class ArimaaPiece(val goldenPlayer: Boolean, val pieceType: PieceType, val resID: Int){

    override fun equals(deepPiece: Any?): Boolean {
        if (this === deepPiece) return true
        if (deepPiece !is ArimaaPiece) return false
        return goldenPlayer == deepPiece.goldenPlayer && pieceType == deepPiece.pieceType
    }

    override fun hashCode(): Int {
        var result = goldenPlayer.hashCode()
        result = 31 * result + (pieceType.hashCode() ?: 0)
//        result = 31 * result + power
        return result
    }

    fun ErrorMessages(fromRow: Int, fromCol: Int, toRow: Int, toCol: Int, goldenPlayer: Boolean): String? {
        if (fromRow == toRow && fromCol == toCol) return "Start and end positions cannot be same."
        if (fromRow != toRow && fromCol != toCol) return "Diagonal move is not permitted"
        val steps = abs(fromRow - toRow) + abs(fromCol - toCol)
        if (steps > 1) return "You can move only one step."
        if (fromCol == toCol) return null
        if (pieceType == PieceType.Rabbit) {
            if (goldenPlayer != goldenPlayer) return null
            val yOffset = toCol - fromCol
            if (goldenPlayer && yOffset == 1) return null
            return if (!goldenPlayer && yOffset == -1) null else "Rabbits cannot move backwards!"
        }
        return null
    }
}