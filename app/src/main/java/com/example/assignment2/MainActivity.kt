package com.example.assignment2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity(),ArimaaDelegate {

    private var arimaaGame: ArimaaGame? = null
    private lateinit var finishTurnButton: Button
    private lateinit var currentPlayer: TextView
    private lateinit var error: TextView
    private  var customView: CustomView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentPlayer = findViewById<TextView>(R.id.currentPlayer)
        error = findViewById<TextView>(R.id.error)
        finishTurnButton = findViewById<Button>(R.id.finish_button)
       customView = findViewById<CustomView>(R.id.custom_view)

        arimaaGame = ArimaaGame(this)
        customView!!.Start(arimaaGame)
        endTurn(true)



        finishTurnButton.setOnClickListener{
            arimaaGame!!.finishTurn()
        }
    }

    override fun endTurn(goldPlayer: Boolean) {

        if (arimaaGame == null) {
            currentPlayer.text = null
            return
        }
        val current = if (goldPlayer) "Gold" else "Silver"
        error.text = (null)
        currentPlayer.text = ("Current turn: $current")
    }

    override fun onError(errorMessage: String?) {
        error.text = errorMessage
    }


    override fun updateBoard() {
        customView!!.invalidate()
    }

    override fun showMessage(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }



}