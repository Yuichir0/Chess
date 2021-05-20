package com.example.android_chess

import android.os.Build
import androidx.annotation.RequiresApi
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.abs

class ChessBack {
    var pieceBox = mutableSetOf<ChessPiece>()
    var whiteTurn = true // Переменная для проверки того, кто ходит
    var blackIsCheck = false
    var whiteIsCheck = false

    init {
        reset()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun clearColumn(startColumn: Int, finishColumn: Int, startRow: Int): Boolean {
        val minColumn = min(startColumn, finishColumn)
        val maxColumn = max(startColumn, finishColumn)
        for (i in minColumn + 1 until maxColumn)
            if (square(i, startRow) != null)
                return false
        return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun clearRow(startRow: Int, finishRow: Int, startColumn: Int): Boolean {
        val minRow = min(startRow, finishRow)
        val maxRow = max(startRow, finishRow)
        for (i in minRow + 1 until maxRow)
            if (square(startColumn, i) != null)
                return false
        return true
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun clearDiagonal(startColumn: Int, startRow: Int, finishColumn: Int, finishRow: Int): Boolean {
        val minColumn = min(startColumn, finishColumn)
        val maxColumn = max(startColumn, finishColumn)
        var j = 0
        if (startColumn - finishColumn == startRow - finishRow)
            for (i in minColumn + 1 until maxColumn) {
            j++
            if (square(i, min(startRow, finishRow) + j) != null)
                return false
        } else for (i in minColumn + 1 until maxColumn) {
            j++
            if (square(i, max(startRow, finishRow) - j) != null)
                return false
        }
        return true
    }
    private fun canKnightMove(startColumn: Int, startRow: Int, finishColumn: Int, finishRow: Int): Boolean {
        return (abs(startColumn - finishColumn) == 1 && abs(startRow - finishRow) == 2) ||
        (abs(startColumn - finishColumn) == 2 && abs(startRow - finishRow) == 1)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun canRookMove(startColumn: Int, startRow: Int, finishColumn: Int, finishRow: Int): Boolean {
        return (finishColumn - startColumn == 0 && clearRow(startRow, finishRow, startColumn)) ||
        ((finishRow - startRow == 0) && clearColumn(startColumn, finishColumn, startRow))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun canBishopMove(startColumn: Int, startRow: Int, finishColumn: Int, finishRow: Int): Boolean {
        return abs(finishColumn - startColumn) == abs(finishRow - startRow) &&
               clearDiagonal(startColumn, startRow, finishColumn, finishRow)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun canQueenMove(startColumn: Int, startRow: Int, finishColumn: Int, finishRow: Int): Boolean {
        return canRookMove(startColumn, startRow, finishColumn, finishRow) ||
                canBishopMove(startColumn, startRow, finishColumn, finishRow)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun canKingMove(startColumn: Int, startRow: Int, finishColumn: Int, finishRow: Int): Boolean { // Доделать отсутствие возможности рокироваться при угрозе ладье или королю после хода. Не забыть, что при создании возможности повышения пешки у созданной фигуры должен быть параметр moved = true FIXME
        return (abs(finishColumn - startColumn) <= 1 && abs(finishRow - startRow) <= 1) ||
                (square(startColumn, startRow)?.moved == false && square(startColumn + 1, startRow) == null && square(startColumn + 2, startRow) == null &&
                        square(startColumn + 3, startRow)?.moved == false && (finishColumn - startColumn == 2) && (finishRow - startRow == 0)) ||
                (square(startColumn, startRow)?.moved == false && square(startColumn - 1, startRow) == null && square(startColumn - 2, startRow) == null && square(startColumn - 3, startRow) == null &&
                        square(startColumn - 4, startRow)?.moved == false && (finishColumn - startColumn == -2) && (finishRow - startRow == 0))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun canPawnMove(startColumn: Int, startRow: Int, finishColumn: Int, finishRow: Int): Boolean { // En Passant нет FIXME
        return (square(startColumn, startRow)?.player == ChessPlayer.WHITE && (finishRow - startRow) == 1 && abs (finishColumn - startColumn) == 0 && square(finishColumn, finishRow) == null) ||
                (square(startColumn, startRow)?.player == ChessPlayer.BLACK && (finishRow - startRow) == -1 && abs (finishColumn - startColumn) == 0 && square(finishColumn, finishRow) == null) ||
                (abs(finishColumn - startColumn) == 1 && abs(finishRow - startRow) == 1 && square(finishColumn, finishRow) != null) ||
                (square(startColumn, startRow)?.player == ChessPlayer.WHITE && (finishRow - startRow) == 2 && abs (finishColumn - startColumn) == 0 && square(finishColumn, finishRow) == null &&
                        square(finishColumn, finishRow - 1) == null && square(startColumn, startRow)?.moved == false) ||
                (square(startColumn, startRow)?.player == ChessPlayer.BLACK && (finishRow - startRow) == -2 && abs (finishColumn - startColumn) == 0 && square(finishColumn, finishRow) == null &&
                        square(finishColumn, finishRow + 1) == null && square(startColumn, startRow)?.moved == false)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun movePiece(startColumn: Int, startRow: Int, finishColumn: Int, finishRow: Int) { // Возможны ли ходы фигур без проверок на шах, мат и пат FIXME
        val movingPiece = square(startColumn, startRow) ?: return

        if (whiteTurn && movingPiece.player == ChessPlayer.WHITE && movingPiece != square(finishColumn, finishRow)) {
            if ((canKnightMove(startColumn, startRow, finishColumn, finishRow) &&
                            movingPiece.type == ChessPieceType.KNIGHT) ||
                    (canRookMove(startColumn, startRow, finishColumn, finishRow) &&
                            movingPiece.type == ChessPieceType.ROOK) ||
                    (canBishopMove(startColumn, startRow, finishColumn, finishRow) &&
                            movingPiece.type == ChessPieceType.BISHOP) ||
                    (canQueenMove(startColumn, startRow, finishColumn, finishRow) &&
                            movingPiece.type == ChessPieceType.QUEEN) ||
                    (canKingMove(startColumn, startRow, finishColumn, finishRow) &&
                            movingPiece.type == ChessPieceType.KING) ||
                    (canPawnMove(startColumn, startRow, finishColumn, finishRow) &&
                            movingPiece.type == ChessPieceType.PAWN)) {
                square(finishColumn, finishRow)?.let {
                    if (it.player == movingPiece.player) return
                    pieceBox.remove(it)
                }

                if (finishColumn < 0 || finishColumn > 7 || finishRow < 0 || finishRow > 7) return
                pieceBox.remove(movingPiece)
                pieceBox.add(ChessPiece(finishColumn, finishRow, movingPiece.player, movingPiece.type, movingPiece.pieceType, true))
                whiteTurn = false

                if (movingPiece.type == ChessPieceType.KING && (finishColumn - startColumn) == 2 && startRow == 0) { // Проверка рокировки для перемещения ладьи
                    pieceBox.remove(square(startColumn + 3, startRow))
                    pieceBox.add(ChessPiece(finishColumn - 1, finishRow, movingPiece.player, ChessPieceType.ROOK, R.drawable.wr, true))
                }
                if (movingPiece.type == ChessPieceType.KING && (finishColumn - startColumn) == 2 && startRow == 7) {
                    pieceBox.remove(square(startColumn + 3, startRow))
                    pieceBox.add(ChessPiece(finishColumn - 1, finishRow, movingPiece.player, ChessPieceType.ROOK, R.drawable.br, true))
                }
                if (movingPiece.type == ChessPieceType.KING && (finishColumn - startColumn) == -2 && startRow == 0) {
                    pieceBox.remove(square(startColumn - 4, startRow))
                    pieceBox.add(ChessPiece(finishColumn + 1, finishRow, movingPiece.player, ChessPieceType.ROOK, R.drawable.wr, true))
                }
                if (movingPiece.type == ChessPieceType.KING && (finishColumn - startColumn) == -2 && startRow == 7) {
                    pieceBox.remove(square(startColumn - 4, startRow))
                    pieceBox.add(ChessPiece(finishColumn + 1, finishRow, movingPiece.player, ChessPieceType.ROOK, R.drawable.br, true))
                }
            }
        } else if (!whiteTurn && movingPiece.player != ChessPlayer.WHITE && movingPiece != square(finishColumn, finishRow)) {
            if ((canKnightMove(startColumn, startRow, finishColumn, finishRow) &&
                            movingPiece.type == ChessPieceType.KNIGHT) ||
                    (canRookMove(startColumn, startRow, finishColumn, finishRow) &&
                            movingPiece.type == ChessPieceType.ROOK) ||
                    (canBishopMove(startColumn, startRow, finishColumn, finishRow) &&
                            movingPiece.type == ChessPieceType.BISHOP) ||
                    (canQueenMove(startColumn, startRow, finishColumn, finishRow) &&
                            movingPiece.type == ChessPieceType.QUEEN) ||
                    (canKingMove(startColumn, startRow, finishColumn, finishRow) &&
                            movingPiece.type == ChessPieceType.KING) ||
                    (canPawnMove(startColumn, startRow, finishColumn, finishRow) &&
                            movingPiece.type == ChessPieceType.PAWN)) {
                square(finishColumn, finishRow)?.let {
                    if (it.player == movingPiece.player) return
                    pieceBox.remove(it)
                }

                if (finishColumn < 0 || finishColumn > 7 || finishRow < 0 || finishRow > 7) return
                pieceBox.remove(movingPiece)
                pieceBox.add(ChessPiece(finishColumn, finishRow, movingPiece.player, movingPiece.type, movingPiece.pieceType, true))
                whiteTurn = true

                if (movingPiece.type == ChessPieceType.KING && (finishColumn - startColumn) == 2 && startRow == 0) { // Проверка рокировки для перемещения ладьи
                    pieceBox.remove(square(startColumn + 3, startRow))
                    pieceBox.add(ChessPiece(finishColumn - 1, finishRow, movingPiece.player, ChessPieceType.ROOK, R.drawable.wr, true))
                }
                if (movingPiece.type == ChessPieceType.KING && (finishColumn - startColumn) == 2 && startRow == 7) {
                    pieceBox.remove(square(startColumn + 3, startRow))
                    pieceBox.add(ChessPiece(finishColumn - 1, finishRow, movingPiece.player, ChessPieceType.ROOK, R.drawable.br, true))
                }
                if (movingPiece.type == ChessPieceType.KING && (finishColumn - startColumn) == -2 && startRow == 0) {
                    pieceBox.remove(square(startColumn - 4, startRow))
                    pieceBox.add(ChessPiece(finishColumn + 1, finishRow, movingPiece.player, ChessPieceType.ROOK, R.drawable.wr, true))
                }
                if (movingPiece.type == ChessPieceType.KING && (finishColumn - startColumn) == -2 && startRow == 7) {
                    pieceBox.remove(square(startColumn - 4, startRow))
                    pieceBox.add(ChessPiece(finishColumn + 1, finishRow, movingPiece.player, ChessPieceType.ROOK, R.drawable.br, true))
                }
            }
        }
    }

    fun reset() {
        pieceBox.removeAll(pieceBox)
        for (i in 0..7) pieceBox.add(ChessPiece(i,1, ChessPlayer.WHITE, ChessPieceType.PAWN, R.drawable.wp, false))
        for (i in 0..7) pieceBox.add(ChessPiece(i,6, ChessPlayer.BLACK, ChessPieceType.PAWN, R.drawable.bp, false))
        for (i in 0..1) pieceBox.add(ChessPiece(0 + 7 * i,0, ChessPlayer.WHITE, ChessPieceType.ROOK, R.drawable.wr, false))
        for (i in 0..1) pieceBox.add(ChessPiece(0 + 7 * i,7, ChessPlayer.BLACK, ChessPieceType.ROOK, R.drawable.br, false))
        for (i in 0..1) pieceBox.add(ChessPiece(1 + 5 * i,0, ChessPlayer.WHITE, ChessPieceType.KNIGHT, R.drawable.wh, false))
        for (i in 0..1) pieceBox.add(ChessPiece(1 + 5 * i,7, ChessPlayer.BLACK, ChessPieceType.KNIGHT, R.drawable.bh, false))
        for (i in 0..1) pieceBox.add(ChessPiece(2 + 3 * i,0, ChessPlayer.WHITE, ChessPieceType.BISHOP, R.drawable.wb, false))
        for (i in 0..1) pieceBox.add(ChessPiece(2 + 3 * i,7, ChessPlayer.BLACK, ChessPieceType.BISHOP, R.drawable.bb, false))
        pieceBox.add(ChessPiece(3,0, ChessPlayer.WHITE, ChessPieceType.QUEEN, R.drawable.wq, false))
        pieceBox.add(ChessPiece(3,7, ChessPlayer.BLACK, ChessPieceType.QUEEN, R.drawable.bq, false))
        pieceBox.add(ChessPiece(4,0, ChessPlayer.WHITE, ChessPieceType.KING, R.drawable.wk, false))
        pieceBox.add(ChessPiece(4,7, ChessPlayer.BLACK, ChessPieceType.KING, R.drawable.bk, false))

    }

    fun square(column: Int, row: Int): ChessPiece? {
        for (piece in pieceBox)
            if (column == piece.column && row == piece.row) return piece
        return null
    }

    override fun toString(): String {
        var desc = " "
        for (row in 7 downTo 0) {
            desc += "$row"
            for (column in 0..7) {
                val piece = square(column, row)
                desc += if (piece == null) "  ."
                else {
                    val white = piece.player == ChessPlayer.WHITE
                    when (piece.type) {
                        ChessPieceType.KING -> if (white) " WK" else " BK"
                        ChessPieceType.QUEEN -> if (white) " WQ" else " BQ"
                        ChessPieceType.ROOK -> if (white) " WR" else " BR"
                        ChessPieceType.BISHOP -> if (white) " WB" else " BB"
                        ChessPieceType.KNIGHT -> if (white) " WH" else " BH"
                        ChessPieceType.PAWN -> if (white) " WP" else " BP"
                    }
                }
            }
            desc += "\n"
        }
        desc += "  0  1  2  3  4  5  6  7"

        return desc
    }
}