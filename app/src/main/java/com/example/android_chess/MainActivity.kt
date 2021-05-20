package com.example.android_chess

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), ChessConnector {

    private var chessBack = ChessBack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "$chessBack")
        var tiltSecure = 0
        val chessFront: ChessFront = findViewById<ChessFront>(R.id.chess_view)
        chessFront.chessConnector = this
        findViewById<Button>(R.id.reset_button).setOnClickListener {
            tiltSecure++
            if (tiltSecure >= 9) {
                chessBack.reset()
                chessBack.blackIsCheck = false
                chessBack.whiteIsCheck = false
                chessBack.whiteTurn = true
                chessFront.invalidate()
                tiltSecure = 0
            }
        }
        findViewById<Button>(R.id.previous_button).setOnClickListener {
            chessBack.previousTurn()
            chessBack.whiteTurn = !chessBack.whiteTurn
            chessFront.invalidate()
        }
    }

    override fun square(column: Int, row: Int): ChessPiece? {
        return chessBack.square(column, row)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun movePiece(startColumn: Int, startRow: Int, finishColumn: Int, finishRow: Int) {
        chessBack.movePiece(startColumn, startRow, finishColumn, finishRow)
        findViewById<ChessFront>(R.id.chess_view).invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun movePieceHidden(startColumn: Int, startRow: Int, finishColumn: Int, finishRow: Int) {
        chessBack.movePieceHidden(startColumn, startRow, finishColumn, finishRow)
    }
}