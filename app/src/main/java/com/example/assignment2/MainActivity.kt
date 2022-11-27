package com.example.assignment2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(),ArimaaDelegate {

    var arimaaGame = ArimaaGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       val arimaaView = findViewById<CustomView>(R.id.custom_view)
        arimaaView.arimaaDelegate = this
    }

    override fun pieceAt(col: Int, row: Int): ArimaaPiece? {
        return arimaaGame.pieceAt(col, row)
    }

    override fun movePiece(fromCol: Int , fromRow: Int ,toCol: Int, toRow: Int) {
         arimaaGame.movePiece(fromCol,fromRow,toCol,toRow)
        val arimaaView = findViewById<CustomView>(R.id.custom_view)
        arimaaView.invalidate()
    }

}