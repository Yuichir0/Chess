package com.example.android_chess

interface ChessConnector {
    fun square(column: Int, row: Int): ChessPiece?
    fun movePiece(startColumn: Int, startRow: Int, finishColumn: Int, finishRow: Int)
    fun checkCheck()
}