package com.example.assignment2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity(),ArimaaDelegate {

    private lateinit var arimaaGame: ArimaaGame
    private lateinit var finishTurnButton: Button
    private lateinit var resetButton: Button
    private lateinit var undoButton: ImageButton
    private lateinit var currentPlayer: TextView
    private lateinit var error: TextView
    private lateinit var winner: TextView
    private lateinit var customView: CustomView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentPlayer = findViewById<TextView>(R.id.currentPlayer)
        resetButton = findViewById<Button>(R.id.reset_button)
        undoButton = findViewById<ImageButton>(R.id.undo_button)
        error = findViewById<TextView>(R.id.error)
        finishTurnButton = findViewById<Button>(R.id.finish_button)
        customView = findViewById<CustomView>(R.id.custom_view)
        winner = findViewById<TextView>(R.id.Winner)


        arimaaGame = ArimaaGame(this)
        customView.Start(arimaaGame)
        endTurn(true)

        finishTurnButton.setOnClickListener{
            arimaaGame.finishTurn()
        }

        resetButton.setOnClickListener{
            arimaaGame = ArimaaGame(this)
            customView.Start(arimaaGame)
            endTurn(true)
        }

        undoButton.setOnClickListener{
            arimaaGame.undo()
        }
    }


    override fun endTurn(goldPlayer: Boolean) {

        val current = if (goldPlayer) "Gold" else "Silver"
        error.text = (null)
        currentPlayer.text = ("Current turn: $current")
    }

    override fun onError(errorMessage: String?) {
        error.text = errorMessage
    }


    override fun updateBoard() {
        customView.invalidate()
    }

    override fun AlertBeforePull() {
        AlertDialog.Builder(this).setTitle("Pull").setMessage("Do you want to pull enemy piece?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ -> arimaaGame.alretDialogPull(true)}
            .setNegativeButton("No") { _, _ -> arimaaGame.alretDialogPull(false)}
            .show()
    }

    override fun showMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    override fun endGame(goldPlayer: Boolean) {
        winner.text = if(goldPlayer) "Golden Player Wins" else "Silver Player Wins"
        finishTurnButton.isEnabled = false
    }



}