package com.example.android_chess

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), ChessConnector {

    private var chessBack = ChessBack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "$chessBack")
        val chessFront: ChessFront = findViewById(R.id.chess_view)
        chessFront.chessConnector = this
        findViewById<Button>(R.id.reset_button).setOnClickListener {
            chessBack.reset()
            chessFront.invalidate()
        }
    }

    override fun square(x: Int, y: Int): ChessPiece? {
        return chessBack.square(x, y)
    }

    override fun moveFromPlayer(moveFromPlayer: Move) {
        val fromX = moveFromPlayer.fromX
        val fromY = moveFromPlayer.fromY
        val toX = moveFromPlayer.toX
        val toY = moveFromPlayer.toY
        chessBack.moveFromPlayer(Move(fromX, fromY, toX, toY))
        if (chessBack.whiteWin) Toast.makeText(applicationContext, "White win!", Toast.LENGTH_LONG).show()
        if (chessBack.blackWin) Toast.makeText(applicationContext, "Black win!", Toast.LENGTH_LONG).show()
        if (chessBack.pat) Toast.makeText(applicationContext, "Everyone lost!", Toast.LENGTH_LONG).show()
        findViewById<ChessFront>(R.id.chess_view).invalidate()
    }
}