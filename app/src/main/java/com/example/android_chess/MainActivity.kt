package com.example.android_chess

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
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
                chessBack.blackIsCheck = false
                chessBack.whiteIsCheck = false
                chessBack.whiteTurn = true
                chessFront.invalidate()
            Log.d(TAG, "New game")
        }
        findViewById<Button>(R.id.previous_button).setOnClickListener {
            if (chessBack.moveHistory.isNotEmpty()) {
                chessBack.previousTurn()
                chessFront.invalidate()
                Log.d(TAG, "Previous move")
            }
        }
    }

    override fun square(column: Int, row: Int): ChessPiece? {
        return chessBack.square(column, row)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun movePiece(startColumn: Int, startRow: Int, finishColumn: Int, finishRow: Int) {
        chessBack.movePiece(startColumn, startRow, finishColumn, finishRow)
        findViewById<ChessFront>(R.id.chess_view).invalidate()
        var whiteKingAlive = false
        chessBack.pieceBox.forEach {
            if (it.player == ChessPlayer.WHITE && it.type == ChessPieceType.KING)
                whiteKingAlive = true
        }
        var blackKingAlive = false
        chessBack.pieceBox.forEach {
            if (it.player == ChessPlayer.BLACK && it.type == ChessPieceType.KING)
                blackKingAlive = true
        }
        if (!whiteKingAlive) Toast.makeText(applicationContext, "Black win!", Toast.LENGTH_LONG).show()
        if (!blackKingAlive) Toast.makeText(applicationContext, "White win!", Toast.LENGTH_LONG).show()
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun checkCheck() {
        chessBack.checkCheck()
    }
}