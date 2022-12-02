package com.example.assignment2

import java.util.ArrayList

class ArimaaBoard {
    private var arimaaPieces: Array<Array<ArimaaPiece?>?> = Array(8) { arrayOfNulls(8) }
    private var trappedPieces: MutableList<ArimaaPiece?> = ArrayList()
    fun getArimaaPieces(): Array<Array<ArimaaPiece?>?> {
        return arimaaPieces
    }

    fun State(): BoardState {
        return BoardState(DeepCopyArimaaPieces(arimaaPieces), DeepCopyTrap(trappedPieces))
    }

    private fun DeepCopyArimaaPieces(arimaaPieces: Array<Array<ArimaaPiece?>?>?): Array<Array<ArimaaPiece?>?> {
        val copy = Array<Array<ArimaaPiece?>?>(8) { arrayOfNulls(8) }
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                val arimaaPiece = arimaaPieces!![i]!![j] ?: continue
                copy[i]!![j] = ArimaaPiece(arimaaPiece.goldenPlayer, arimaaPiece.pieceType,arimaaPiece.resID)
            }
        }
        return copy
    }

    private fun DeepCopyTrap(trap: List<ArimaaPiece?>?): MutableList<ArimaaPiece?> {
        val copy: MutableList<ArimaaPiece?> = ArrayList()
        for (arimaaPiece in trap!!) {
            copy.add(ArimaaPiece(arimaaPiece!!.goldenPlayer, arimaaPiece.pieceType,arimaaPiece.resID))
        }
        return copy
    }

    init {

        for (col in 0 until 8) {
            arimaaPieces[col]!![0] = ArimaaPiece(true, PieceType.Rabbit,R.drawable.gold_rabbit)
            arimaaPieces[col]!![7] = ArimaaPiece(false, PieceType.Rabbit, R.drawable.silver_rabbit)
        }

        arimaaPieces[0]!![1] = ArimaaPiece(true, PieceType.Cat,R.drawable.gold_cat)
        arimaaPieces[1]!![1] = ArimaaPiece(true, PieceType.Dog, R.drawable.gold_dog)
        arimaaPieces[2]!![1] = ArimaaPiece(true, PieceType.Horse, R.drawable.gold_horse)
        arimaaPieces[3]!![1] = ArimaaPiece(true, PieceType.Camel, R.drawable.gold_camel)
        arimaaPieces[4]!![1] = ArimaaPiece(true, PieceType.Elephant, R.drawable.gold_elephant)
        arimaaPieces[5]!![1] = ArimaaPiece(true, PieceType.Horse, R.drawable.gold_horse)
        arimaaPieces[6]!![1] = ArimaaPiece(true, PieceType.Dog, R.drawable.gold_dog)
        arimaaPieces[7]!![1] = ArimaaPiece(true, PieceType.Cat, R.drawable.gold_cat)

        arimaaPieces[0]!![6] = ArimaaPiece(false, PieceType.Cat, R.drawable.silver_cat)
        arimaaPieces[1]!![6] = ArimaaPiece(false, PieceType.Dog, R.drawable.silver_dog)
        arimaaPieces[2]!![6] = ArimaaPiece(false, PieceType.Horse, R.drawable.silver_horse)
        arimaaPieces[3]!![6] = ArimaaPiece(false, PieceType.Elephant, R.drawable.silver_elephant)
        arimaaPieces[4]!![6] = ArimaaPiece(false, PieceType.Camel, R.drawable.silver_camel)
        arimaaPieces[5]!![6] = ArimaaPiece(false, PieceType.Horse, R.drawable.silver_horse)
        arimaaPieces[6]!![6] = ArimaaPiece(false, PieceType.Dog, R.drawable.silver_dog)
        arimaaPieces[7]!![6] = ArimaaPiece(false, PieceType.Cat, R.drawable.silver_cat)


    }
}