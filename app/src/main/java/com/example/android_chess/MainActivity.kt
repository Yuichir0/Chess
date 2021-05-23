package com.example.android_chess

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), ChessConnector {

    private var chessBack = ChessBack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "$chessBack")
        val chessFront: ChessFront = findViewById<ChessFront>(R.id.chess_view)
        chessFront.chessConnector = this
        findViewById<Button>(R.id.reset_button).setOnClickListener {
            chessBack.reset()
            chessBack.whiteTurn = true
            chessFront.invalidate()
            Log.d(TAG, "New game")
        }
    }

    override fun square(x: Int, y: Int): ChessPiece? {
        return chessBack.square(x, y)
    }

    override fun move(move: Move) {
        val fromX = move.fromX
        val fromY = move.fromY
        val toX = move.toX
        val toY = move.toY
        chessBack.move(Move(fromX, fromY, toX, toY))
        findViewById<ChessFront>(R.id.chess_view).invalidate()
    }
}