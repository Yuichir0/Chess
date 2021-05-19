package com.example.android_chess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), ChessConnector {

    var chessMain = ChessMain()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "$chessMain")
        findViewById<ChessView>(R.id.chess_view).chessConnector = this
    }

    override fun square(column: Int, row: Int): ChessPiece? {
        return chessMain.square(column, row)
    }

    override fun movePiece(startColumn: Int, startRow: Int, finishColumn: Int, finishRow: Int) {
        chessMain.movePiece(startColumn, startRow, finishColumn, finishRow)
        findViewById<ChessView>(R.id.chess_view).invalidate()
    }
}