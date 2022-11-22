package com.example.assignment2

object ArimaaGame {
    private var piecesBox = mutableSetOf<ArimaaPiece>()

    init {
        reset()
    }

    fun clear() {
        piecesBox.clear()
    }

    fun addPiece(piece: ArimaaPiece) {
        piecesBox.add(piece)
    }


    fun reset() {
        clear()
        for (i in 0 until 2) {
            addPiece(ArimaaPiece(0 + i * 7, 1, Player.Gold, PieceType.Cat, R.drawable.gold_cat))
            addPiece(ArimaaPiece(0 + i * 7, 6, Player.Silver, PieceType.Cat, R.drawable.silver_cat))

            addPiece(ArimaaPiece(1 + i * 5, 1, Player.Gold, PieceType.Horse, R.drawable.gold_horse))
            addPiece(ArimaaPiece(1 + i * 5, 6, Player.Silver, PieceType.Horse, R.drawable.silver_horse))

            addPiece(ArimaaPiece(2 + i * 3, 1, Player.Gold, PieceType.Dog, R.drawable.gold_dog))
            addPiece(ArimaaPiece(2 + i * 3, 6, Player.Silver, PieceType.Dog, R.drawable.silver_dog))
        }

        for (i in 0 until 8) {
            addPiece(ArimaaPiece(i, 0, Player.Gold, PieceType.Rabbit, R.drawable.gold_rabbit))
            addPiece(ArimaaPiece(i, 7, Player.Silver, PieceType.Rabbit, R.drawable.silver_rabbit))
        }

        addPiece(ArimaaPiece(3, 1, Player.Gold, PieceType.Camel, R.drawable.gold_camel))
        addPiece(ArimaaPiece(3, 6, Player.Silver, PieceType.Camel, R.drawable.silver_camel))
        addPiece(ArimaaPiece(4, 1, Player.Gold, PieceType.Elephant, R.drawable.gold_elephant))
        addPiece(ArimaaPiece(4, 6, Player.Silver, PieceType.Elephant, R.drawable.silver_elephant))
    }


     fun pieceAt(col: Int, row: Int): ArimaaPiece? {
        for (piece in piecesBox) {
            if (col == piece.col && row == piece.row) {
                return  piece
            }
        }
        return null
    }
}