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
            chessBack.whiteTurn = true
            chessBack.possibleMoves.clear()
            chessBack.legalMoves.clear()
            chessBack.squareUnderAttack.clear()
            chessBack.kingWhiteSquare = Square(4, 0)
            chessBack.kingBlackSquare = Square(4, 7)
            chessBack.capturedPieces.clear()
            chessBack.lastMove.clear()
            chessBack.pieceBox.clear()
            chessBack.blackIsCheck = false
            chessBack.whiteIsCheck = false
            chessBack.lastMoveSuccessful = false
            chessBack.enPassant = false
            chessBack.pawnPromoted = false
            chessBack.blackWin = false
            chessBack.whiteWin = false
            chessBack.pat = false
            chessBack.reset()
            chessFront.invalidate()
            Log.d(TAG, "New game")
        }
        findViewById<Button>(R.id.previous_button).setOnClickListener {
            chessBack.previousMove()
            chessFront.invalidate()
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

    override fun legalMovesForAll(moveFromPlayer: Move) {
        val fromX = moveFromPlayer.fromX
        val fromY = moveFromPlayer.fromY
        val toX = moveFromPlayer.toX
        val toY = moveFromPlayer.toY
        chessBack.legalMovesForAll(Move(fromX, fromY, toX, toY))
        if (chessBack.whiteWin) Toast.makeText(applicationContext, "White win!", Toast.LENGTH_LONG).show()
        if (chessBack.blackWin) Toast.makeText(applicationContext, "Black win!", Toast.LENGTH_LONG).show()
        if (chessBack.pat) Toast.makeText(applicationContext, "Everyone lost!", Toast.LENGTH_LONG).show()
        findViewById<ChessFront>(R.id.chess_view).invalidate()
    }
}