package com.example.android_chess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), ChessConnector {

    var chessBack = ChessBack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "$chessBack")
        findViewById<ChessFront>(R.id.chess_view).chessConnector = this
    }

    override fun square(column: Int, row: Int): ChessPiece? {
        return chessBack.square(column, row)
    }

    override fun movePiece(startColumn: Int, startRow: Int, finishColumn: Int, finishRow: Int) {
        chessBack.movePiece(startColumn, startRow, finishColumn, finishRow)
        findViewById<ChessFront>(R.id.chess_view).invalidate()
    }
}