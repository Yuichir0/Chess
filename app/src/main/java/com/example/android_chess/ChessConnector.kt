package com.example.android_chess

interface ChessConnector {
    fun square(x: Int, y: Int): ChessPiece?
    fun move(move: Move)
}