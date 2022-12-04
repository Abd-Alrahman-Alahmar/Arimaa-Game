package com.example.assignment2

interface ArimaaDelegate {

    fun onError(errorMessage: String?)

    fun endTurn(goldPlayer: Boolean)

    fun showMessage(message: String?)

    fun updateBoard()

    fun AlertBeforePull()

    fun endGame(goldPlayer: Boolean)

}