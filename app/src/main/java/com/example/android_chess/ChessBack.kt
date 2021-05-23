package com.example.android_chess

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.abs

class ChessBack {
    var pieceBox = mutableSetOf<ChessPiece>() // Список фигур на доске
    var possibleMoves = mutableListOf<Move>() // Возможные ходы без проверки на шах
    var legalMoves = mutableListOf<Move>() // Возможные ходы для игрока
    var squareUnderAttack = mutableListOf<Square>() // Список клеток под атакой
    var whiteTurn = true // Переменная для проверки того, кто ходит
    var capturedPieces = mutableListOf<ChessPiece?>()
    var kingWhiteSquare = Square(4, 0)
    var kingBlackSquare = Square(4, 7)

    init {
        reset()
    }

    private fun possibleMoveRook(piece: ChessPiece, range: Int) {
        var stop1 = false
        var stop2 = false
        var stop3 = false
        var stop4 = false
        for (i in 1..range) {
            if (piece.x - i >= 0 && !stop1) {
                stop1 = true
                if (piece.player != square(piece.x - i, piece.y)?.player && square(piece.x - i, piece.y)?.player != null) {
                    possibleMoves.add(Move(piece.x, piece.y, piece.x - i, piece.y))
                    squareUnderAttack.add(Square(piece.x - i, piece.y))
                }
                if (square(piece.x - i, piece.y) == null) {
                    possibleMoves.add(Move(piece.x, piece.y, piece.x - i, piece.y))
                    stop1 = false
                }
            }

            if (piece.x + i <= 7 && !stop2) {
                stop2 = true
                if (piece.player != square(piece.x + i, piece.y)?.player && square(piece.x + i, piece.y)?.player != null) {
                    possibleMoves.add(Move(piece.x, piece.y, piece.x + i, piece.y))
                    squareUnderAttack.add(Square(piece.x + i, piece.y))
                }
                if (square(piece.x + i, piece.y) == null) {
                    possibleMoves.add(Move(piece.x, piece.y, piece.x + i, piece.y))
                    stop2 = false
                }
            }

            if (piece.y - i >= 0 && !stop3) {
                stop3 = true
                if (piece.player != square(piece.x, piece.y - i)?.player && square(piece.x, piece.y - i)?.player != null) {
                    possibleMoves.add(Move(piece.x, piece.y, piece.x, piece.y - i))
                    squareUnderAttack.add(Square(piece.x, piece.y - i))
                }
                if (square(piece.x, piece.y - i) == null) {
                    possibleMoves.add(Move(piece.x, piece.y, piece.x, piece.y - i))
                    stop3 = false
                }
            }

            if (piece.y + i <= 7 && !stop4) {
                stop4 = true
                if (piece.player != square(piece.x, piece.y + i)?.player && square(piece.x, piece.y + i)?.player != null) {
                    possibleMoves.add(Move(piece.x, piece.y, piece.x, piece.y + i))
                    squareUnderAttack.add(Square(piece.x, piece.y + i))
                }
                if (square(piece.x, piece.y + i) == null) {
                    possibleMoves.add(Move(piece.x, piece.y, piece.x, piece.y + i))
                    stop4 = false
                }
            }
        }
    }

    private fun possibleMoveBishop(piece: ChessPiece, range: Int) {
        var stop1 = false
        var stop2 = false
        var stop3 = false
        var stop4 = false
        for (i in 1..range) {
            if (piece.x - i >= 0 && piece.y - i >= 0 && !stop1) {
                stop1 = true
                if (piece.player != square(piece.x - i, piece.y - i)?.player && square(piece.x - i, piece.y - i)?.player != null) {
                    possibleMoves.add(Move(piece.x, piece.y, piece.x - i, piece.y - i))
                    squareUnderAttack.add(Square(piece.x - i, piece.y - i))
                }
                if (square(piece.x - i, piece.y - i) == null) {
                    possibleMoves.add(Move(piece.x, piece.y, piece.x - i, piece.y - i))
                    stop1 = false
                }
            }

            if (piece.x + i >= 0 && piece.y - i >= 0 && !stop2) {
                stop2 = true
                if (piece.player != square(piece.x + i, piece.y - i)?.player && square(piece.x + i, piece.y - i)?.player != null) {
                    possibleMoves.add(Move(piece.x, piece.y, piece.x + i, piece.y - i))
                    squareUnderAttack.add(Square(piece.x + i, piece.y - i))
                }
                if (square(piece.x + i, piece.y - i) == null) {
                    possibleMoves.add(Move(piece.x, piece.y, piece.x + i, piece.y - i))
                    stop2 = false
                }
            }

            if (piece.x - i >= 0 && piece.y + i >= 0 && !stop3) {
                stop3 = true
                if (piece.player != square(piece.x - i, piece.y + i)?.player && square(piece.x - i, piece.y + i)?.player != null) {
                    possibleMoves.add(Move(piece.x, piece.y, piece.x - i, piece.y + i))
                    squareUnderAttack.add(Square(piece.x - i, piece.y + i))
                }
                if (square(piece.x - i, piece.y + i) == null) {
                    possibleMoves.add(Move(piece.x, piece.y, piece.x - i, piece.y + i))
                    stop3 = false
                }
            }

            if (piece.x + i >= 0 && piece.y + i >= 0 && !stop4) {
                stop4 = true
                if (piece.player != square(piece.x + i, piece.y + i)?.player && square(piece.x + i, piece.y + i)?.player != null) {
                    possibleMoves.add(Move(piece.x, piece.y, piece.x + i, piece.y + i))
                    squareUnderAttack.add(Square(piece.x + i, piece.y + i))
                }
                if (square(piece.x + i, piece.y + i) == null) {
                    possibleMoves.add(Move(piece.x, piece.y, piece.x + i, piece.y + i))
                    stop4 = false
                }
            }
        }
    }

    private fun possibleMoveQueen(piece: ChessPiece) {
        possibleMoveRook(piece, 7)
        possibleMoveBishop(piece, 7)
    }

    private fun possibleMoveKing(piece: ChessPiece) { //TODO
        possibleMoveRook(piece, 1)
        possibleMoveBishop(piece, 1)
    }

    private fun possibleMoveKnight(piece: ChessPiece) {
        if (piece.x - 1 >= 0 && piece.y - 2 >= 0) {
            if (piece.player != square(piece.x - 1, piece.y - 2)?.player && square(piece.x - 1, piece.y - 2)?.player != null) {
                possibleMoves.add(Move(piece.x, piece.y, piece.x - 1, piece.y - 2))
                squareUnderAttack.add(Square(piece.x - 1, piece.y - 2))
            }
            if (square(piece.x - 1, piece.y - 2) == null) possibleMoves.add(Move(piece.x, piece.y, piece.x - 1, piece.y - 2))
        }

        if (piece.x - 2 >= 0 && piece.y - 1 >= 0) {
            if (piece.player != square(piece.x - 2, piece.y - 1)?.player && square(piece.x - 2, piece.y - 1)?.player != null) {
                possibleMoves.add(Move(piece.x, piece.y, piece.x - 2, piece.y - 1))
                squareUnderAttack.add(Square(piece.x - 2, piece.y - 1))
            }
        if (square(piece.x - 2, piece.y - 1) == null) possibleMoves.add(Move(piece.x, piece.y, piece.x - 2, piece.y - 1))
        }

        if (piece.x + 1 <= 7 && piece.y - 2 >= 0) {
            if (piece.player != square(piece.x + 1, piece.y - 2)?.player && square(piece.x + 1, piece.y - 2)?.player != null) {
                possibleMoves.add(Move(piece.x, piece.y, piece.x + 1, piece.y - 2))
                squareUnderAttack.add(Square(piece.x + 1, piece.y - 2))
            }
            if (square(piece.x + 1, piece.y - 2) == null) possibleMoves.add(Move(piece.x, piece.y, piece.x + 1, piece.y - 2))
        }

        if (piece.x + 2 <= 7 && piece.y - 1 >= 0) {
            if (piece.player != square(piece.x + 2, piece.y - 1)?.player && square(piece.x + 2, piece.y - 1)?.player != null) {
                possibleMoves.add(Move(piece.x, piece.y, piece.x + 2, piece.y - 1))
                squareUnderAttack.add(Square(piece.x + 2, piece.y - 1))
            }
            if (square(piece.x + 2, piece.y - 1) == null) possibleMoves.add(Move(piece.x, piece.y, piece.x + 2, piece.y - 1))
        }

        if (piece.x - 1 >= 0 && piece.y + 2 <= 7) {
            if (piece.player != square(piece.x - 1, piece.y + 2)?.player && square(piece.x - 1, piece.y + 2)?.player != null) {
                possibleMoves.add(Move(piece.x, piece.y, piece.x - 1, piece.y + 2))
                squareUnderAttack.add(Square(piece.x - 1, piece.y + 2))
            }
            if (square(piece.x - 1, piece.y + 2) == null) possibleMoves.add(Move(piece.x, piece.y, piece.x - 1, piece.y + 2))
        }

        if (piece.x - 2 >= 0 && piece.y + 1 <= 7) {
            if (piece.player != square(piece.x - 2, piece.y + 1)?.player && square(piece.x - 2, piece.y + 1)?.player != null) {
                possibleMoves.add(Move(piece.x, piece.y, piece.x - 2, piece.y + 1))
                squareUnderAttack.add(Square(piece.x - 2, piece.y + 1))
            }
            if (square(piece.x - 2, piece.y + 1) == null) possibleMoves.add(Move(piece.x, piece.y, piece.x - 2, piece.y + 1))
        }

        if (piece.x + 1 <= 7 && piece.y + 2 <= 7) {
            if (piece.player != square(piece.x + 1, piece.y + 2)?.player && square(piece.x + 1, piece.y + 2)?.player != null) {
                possibleMoves.add(Move(piece.x, piece.y, piece.x + 1, piece.y + 2))
                squareUnderAttack.add(Square(piece.x + 1, piece.y + 2))
            }
            if (square(piece.x + 1, piece.y + 2) == null) possibleMoves.add(Move(piece.x, piece.y, piece.x + 1, piece.y + 2))
        }

        if (piece.x + 2 <= 7 && piece.y + 1 <= 7) {
            if (piece.player != square(piece.x + 2, piece.y + 1)?.player && square(piece.x + 2, piece.y + 1)?.player != null) {
                possibleMoves.add(Move(piece.x, piece.y, piece.x + 2, piece.y + 1))
                squareUnderAttack.add(Square(piece.x + 2, piece.y + 1))
            }
            if (square(piece.x + 2, piece.y + 1) == null) possibleMoves.add(Move(piece.x, piece.y, piece.x + 2, piece.y + 1))
        }
    }

    private fun possibleMovePawn(piece: ChessPiece) {
        // Белые пешки
        if (piece.player == ChessPlayer.WHITE && piece.y + 1 <= 7 && (square(piece.x, piece.y + 1) == null)) possibleMoves.add(Move(piece.x, piece.y, piece.x, piece.y + 1))
        if (piece.player == ChessPlayer.WHITE && piece.y + 1 <= 7 && piece.x + 1 <= 7 && piece.player != square(piece.x + 1, piece.y + 1)?.player && square(piece.x + 1, piece.y + 1)?.player != null) {
            possibleMoves.add(Move(piece.x, piece.y, piece.x + 1, piece.y + 1))
            squareUnderAttack.add(Square(piece.x + 1, piece.y + 1))
        }

        if (piece.player == ChessPlayer.WHITE && piece.y + 1 <= 7 && piece.x - 1 >= 0 && piece.player != square(piece.x - 1, piece.y + 1)?.player && square(piece.x - 1, piece.y + 1)?.player != null) {
            possibleMoves.add(Move(piece.x, piece.y, piece.x - 1, piece.y + 1))
            squareUnderAttack.add(Square(piece.x - 1, piece.y + 1))
        }

        if (piece.player == ChessPlayer.WHITE && !piece.moved && square(piece.x, piece.y + 1) == null) possibleMoves.add(Move(piece.x, piece.y, piece.x, piece.y + 2))

        // Черные пешки
        if (piece.player == ChessPlayer.BLACK && piece.y - 1 >= 0 && (square(piece.x, piece.y - 1) == null)) possibleMoves.add(Move(piece.x, piece.y, piece.x, piece.y - 1))
        if (piece.player == ChessPlayer.BLACK && piece.y - 1 >= 0 && piece.x + 1 <= 7 && piece.player != square(piece.x + 1, piece.y - 1)?.player && square(piece.x + 1, piece.y - 1)?.player != null) {
            possibleMoves.add(Move(piece.x, piece.y, piece.x + 1, piece.y - 1))
            squareUnderAttack.add(Square(piece.x + 1, piece.y - 1))
        }

        if (piece.player == ChessPlayer.BLACK && piece.y - 1 >= 0 && piece.x - 1 >= 0 && piece.player != square(piece.x - 1, piece.y - 1)?.player && square(piece.x - 1, piece.y - 1)?.player != null) {
            possibleMoves.add(Move(piece.x, piece.y, piece.x - 1, piece.y - 1))
            squareUnderAttack.add(Square(piece.x - 1, piece.y - 1))
        }

        if (piece.player == ChessPlayer.BLACK && !piece.moved && square(piece.x, piece.y - 1) == null) possibleMoves.add(Move(piece.x, piece.y, piece.x, piece.y - 2))
    }

    fun move(move: Move) {
        val fromX = move.fromX
        val fromY = move.fromY
        val toX = move.toX
        val toY = move.toY
        val movingPiece = square(fromX, fromY) ?: return
        possibleMoves.clear()
        capturedPieces.clear()
        squareUnderAttack.clear()

        pieceBox.forEach { // Заполнение possibleMoves
            if (it.type == ChessPieceType.ROOK) possibleMoveRook(it, 7)
            if (it.type == ChessPieceType.BISHOP) possibleMoveBishop(it, 7)
            if (it.type == ChessPieceType.QUEEN) possibleMoveQueen(it)
            if (it.type == ChessPieceType.KING) possibleMoveKing(it)
            if (it.type == ChessPieceType.KNIGHT) possibleMoveKnight(it)
            if (it.type == ChessPieceType.PAWN) possibleMovePawn(it)
        }
        Log.d(TAG, "$possibleMoves")

        if (move in possibleMoves) {
            Log.d(TAG, "$move")
            if (Square(toX, toY) in squareUnderAttack) {
                Log.d(TAG, "Tried to attack")
                capturedPieces.add(square(toX, toY))
                pieceBox.remove(square(toX, toY))
            }
            pieceBox.add(ChessPiece(toX, toY, movingPiece.player, movingPiece.type, movingPiece.pieceType, true))
            pieceBox.remove(movingPiece)
        }
    }

    fun reset() { // Заполняет коробку фигурами
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

    fun square(x: Int, y: Int): ChessPiece? { // Возвращает фигуру по клетке
        for (piece in pieceBox)
            if (x == piece.x && y == piece.y) return piece
        return null
    }

    override fun toString(): String { // Вывод доски в Logcat
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