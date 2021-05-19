package com.example.android_chess

interface ChessConnector {
    fun square(column: Int, row: Int): ChessPiece?
}