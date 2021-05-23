package com.example.android_chess

data class ChessPiece(val x: Int, val y: Int, val player: ChessPlayer, val type: ChessPieceType, val pieceType: Int, val moved: Boolean) {
// type - вид фигуры, pieceType - картинка
}
