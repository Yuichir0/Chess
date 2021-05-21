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
        var tiltSecure = 0
        val chessFront: ChessFront = findViewById<ChessFront>(R.id.chess_view)
        chessFront.chessConnector = this
        findViewById<Button>(R.id.reset_button).setOnClickListener {
                chessBack.reset()
//                chessBack.blackIsCheck = false
//                chessBack.whiteIsCheck = false
                chessBack.whiteTurn = true
                chessFront.invalidate()
        }
        findViewById<Button>(R.id.previous_button).setOnClickListener {
            if (chessBack.moveHistory.isNotEmpty()) {
                chessBack.previousTurn()
                chessBack.whiteTurn = !chessBack.whiteTurn
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
        if (chessBack.whiteTurn && chessBack.square(chessBack.kingWhiteSquare.first, chessBack.kingWhiteSquare.second) == square(finishColumn, finishRow)) Toast.makeText(applicationContext, "Black win!", Toast.LENGTH_LONG).show()
        if (!chessBack.whiteTurn && chessBack.square(chessBack.kingBlackSquare.first, chessBack.kingBlackSquare.second) == square(finishColumn, finishRow)) Toast.makeText(applicationContext, "White win!", Toast.LENGTH_LONG).show()
    }


//    @RequiresApi(Build.VERSION_CODES.N)
//    override fun movePieceHidden() {
//        chessBack.movePieceHidden()
//    }
}