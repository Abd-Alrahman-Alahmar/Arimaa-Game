package com.example.assignment2

object ArimaaGame {
    private var piecesBox = mutableSetOf<ArimaaPiece>()
    private var GoldPlayer : Player = Player.Gold
    var numberOfMove = 0
    init {
        reset()
    }

    fun clear() {
        piecesBox.clear()
    }

    fun addPiece(piece: ArimaaPiece) {
        piecesBox.add(piece)
    }

    private fun canFirstLineMove(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {

        return toCol == fromCol + 1 && toRow == fromRow ||  toCol == fromCol - 1 && toRow == fromRow ||
               toRow == fromRow + 1 && toCol == fromCol || toRow == fromRow - 1 && toCol == fromCol
    }

    private fun canRabbitMove(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int): Boolean {
       return ( toCol == fromCol + 1 && toRow == fromRow ||  toCol == fromCol - 1 && toRow == fromRow ||
                toRow == fromRow + 1 && toCol == fromCol)


    }

     fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {

         val movingPiece = pieceAt(fromCol, fromRow) ?: return
             if (fromCol == toCol && fromRow == toRow || movingPiece.player != GoldPlayer ||
                 !canFirstLineMove(fromCol, fromRow, toCol, toRow) && movingPiece.pieceType != PieceType.Rabbit ||
                 movingPiece.pieceType == PieceType.Rabbit && !canRabbitMove(fromCol, fromRow, toCol,toRow) ) return


// if move piece on same color piece then don't accept it
             pieceAt(toCol, toRow)?.let {

                 if (it.player == movingPiece.player) {
                     return
                 }
                 piecesBox.remove(it)

             }

//when piece capture another one then delete it
            if( piecesBox.remove(movingPiece) && piecesBox.add (
                    ArimaaPiece(
                        toCol,
                        toRow,
                        movingPiece.player,
                        movingPiece.pieceType,
                        movingPiece.resID
                    )
                ))
                 numberOfMove++
                 if (numberOfMove ==4) {GoldPlayer = Player.Silver}
                 else if (numberOfMove == 8) {GoldPlayer = Player.Gold
                 numberOfMove = 0}

                 println("this is numberofmove value " + numberOfMove)

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