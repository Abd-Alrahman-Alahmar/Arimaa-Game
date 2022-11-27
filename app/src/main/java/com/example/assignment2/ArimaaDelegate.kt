package com.example.assignment2

interface ArimaaDelegate {
    fun pieceAt(col: Int, row: Int) : ArimaaPiece?
    fun movePiece(fromCol: Int , fromRow: Int ,toCol: Int, toRow: Int)
}