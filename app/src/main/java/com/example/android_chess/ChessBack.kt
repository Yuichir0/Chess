package com.example.android_chess

import android.util.Log

class ChessBack {
    var pieceBox = mutableSetOf<ChessPiece>() // Список фигур на доске
    var possibleMoves = mutableListOf<Move>() // Возможные ходы без проверки на шах
    var legalMoves = mutableListOf<Move>() // Возможные ходы для игрока
    var squareUnderAttack = mutableListOf<Square>() // Список клеток под атакой
    var whiteTurn = true // Переменная для проверки того, кто ходит
    var kingWhiteSquare = Square(4, 0)
    var kingBlackSquare = Square(4, 7)
    var capturedPieces = mutableListOf<ChessPiece?>() // Съеденные фигуры (используется только последняя)
    var lastMove = mutableListOf<Move>() // Последний ход (пусть и лист, но используется только последний)
    var blackIsCheck = false
    var whiteIsCheck = false
    var lastMoveSuccessful = false
    var pieceMoved = true
    var pawnPromoted = false
    var enPassant = false

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

    private fun possibleMoveKing(piece: ChessPiece) { //TODO нельзя рокироваться при шахе
        possibleMoveRook(piece, 1)
        possibleMoveBishop(piece, 1)
        if (!piece.moved && piece.player == ChessPlayer.WHITE && !whiteIsCheck) {
            if (square(5,0) == null && square(6,0) == null && square(7,0)?.moved == false) possibleMoves.add(Move(piece.x, piece.y, 6,0))
            if (square(3,0) == null && square(2,0) == null && square(1,0) == null && square(0,0)?.moved == false) possibleMoves.add(Move(piece.x, piece.y, 2,0))
        }
        if (!piece.moved && piece.player == ChessPlayer.BLACK && !blackIsCheck) {
            if (square(5,7) == null && square(6,7) == null && square(7,7)?.moved == false) possibleMoves.add(Move(piece.x, piece.y, 6,7))
            if (square(3,7) == null && square(2,7) == null && square(1,7) == null && square(0,7)?.moved == false) possibleMoves.add(Move(piece.x, piece.y, 2,7))
        }
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

        if (lastMove.isNotEmpty()) {
            val lastMoveP2 = lastMove[lastMove.lastIndex]
            if (lastMoveP2.toY == 4 && lastMoveP2.fromY == 6 && square(lastMoveP2.toX, lastMoveP2.toY)?.type == ChessPieceType.PAWN &&
                    piece.player == ChessPlayer.WHITE && piece.y + 1 <= 7 && piece.x + 1 <= 7 && piece.x == lastMoveP2.toX - 1 && piece.y == lastMoveP2.toY) {
                possibleMoves.add(Move(piece.x, piece.y, piece.x + 1, piece.y + 1))
                squareUnderAttack.add(Square(piece.x + 1, piece.y + 1))
            }
            if (lastMoveP2.toY == 4 && lastMoveP2.fromY == 6 && square(lastMoveP2.toX, lastMoveP2.toY)?.type == ChessPieceType.PAWN &&
                    piece.player == ChessPlayer.WHITE && piece.y + 1 <= 7 && piece.x - 1 <= 7 && piece.x == lastMoveP2.toX + 1 && piece.y == lastMoveP2.toY) {
                possibleMoves.add(Move(piece.x, piece.y, piece.x - 1, piece.y + 1))
                squareUnderAttack.add(Square(piece.x - 1, piece.y + 1))
            }
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

        if (lastMove.isNotEmpty()) {
            val lastMoveP2 = lastMove[lastMove.lastIndex]
            if (lastMoveP2.toY == 3 && lastMoveP2.fromY == 1 && square(lastMoveP2.toX, lastMoveP2.toY)?.type == ChessPieceType.PAWN &&
                    piece.player == ChessPlayer.BLACK && piece.y - 1 >= 0 && piece.x + 1 <= 7 && piece.x == lastMoveP2.toX - 1 && piece.y == lastMoveP2.toY) {
                possibleMoves.add(Move(piece.x, piece.y, piece.x + 1, piece.y - 1))
                squareUnderAttack.add(Square(piece.x + 1, piece.y - 1))
            }
            if (lastMoveP2.toY == 3 && lastMoveP2.fromY == 1 && square(lastMoveP2.toX, lastMoveP2.toY)?.type == ChessPieceType.PAWN &&
                    piece.player == ChessPlayer.BLACK && piece.y - 1 >= 0 && piece.x - 1 >= 0 && piece.x == lastMoveP2.toX + 1 && piece.y == lastMoveP2.toY) {
                possibleMoves.add(Move(piece.x, piece.y, piece.x - 1, piece.y - 1))
                squareUnderAttack.add(Square(piece.x - 1, piece.y - 1))
            }
        }
        if (piece.player == ChessPlayer.BLACK && piece.y - 1 >= 0 && piece.x - 1 >= 0 && piece.player != square(piece.x - 1, piece.y - 1)?.player && square(piece.x - 1, piece.y - 1)?.player != null) {
            possibleMoves.add(Move(piece.x, piece.y, piece.x - 1, piece.y - 1))
            squareUnderAttack.add(Square(piece.x - 1, piece.y - 1))
        }


        if (piece.player == ChessPlayer.BLACK && !piece.moved && square(piece.x, piece.y - 1) == null) possibleMoves.add(Move(piece.x, piece.y, piece.x, piece.y - 2))
    }

    private fun possibleMovesForAll() {
        possibleMoves.clear()
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
    }

    private fun kingUnderCheck() {
        if (kingBlackSquare in squareUnderAttack) blackIsCheck = true
        if (kingWhiteSquare in squareUnderAttack) whiteIsCheck = true
    }

    fun move(move: Move) {
        val fromX = move.fromX
        val fromY = move.fromY
        val toX = move.toX
        val toY = move.toY
        val movingPiece = square(fromX, fromY) ?: return
        blackIsCheck = false
        whiteIsCheck = false
        lastMoveSuccessful = false
        enPassant = false
        possibleMovesForAll()
        kingUnderCheck()
        possibleMovesForAll()
        if (move in possibleMoves)
            if ((whiteTurn && movingPiece.player == ChessPlayer.WHITE) || (!whiteTurn && movingPiece.player == ChessPlayer.BLACK)) {
            Log.d(TAG, "$move")
            pieceMoved = movingPiece.moved
            // Проверка, сделал ли игрок рокировку
            if (fromX == 4 && fromY == 0 && kingWhiteSquare == Square(fromX, fromY) && toX == 6 && toY == 0) {
                pieceBox.add(ChessPiece(5, 0, movingPiece.player, ChessPieceType.ROOK, R.drawable.wr, true))
                pieceBox.remove(square(7,0))
            }
            if (fromX == 4 && fromY == 0 && kingWhiteSquare == Square(fromX, fromY) && toX == 2 && toY == 0) {
                pieceBox.add(ChessPiece(3, 0, movingPiece.player, ChessPieceType.ROOK, R.drawable.wr, true))
                pieceBox.remove(square(0,0))
            }
            if (fromX == 4 && fromY == 7 && kingBlackSquare == Square(fromX, fromY) && toX == 6 && toY == 7) {
                pieceBox.add(ChessPiece(5, 7, movingPiece.player, ChessPieceType.ROOK, R.drawable.br, true))
                pieceBox.remove(square(7,7))
            }
            if (fromX == 4 && fromY == 7 && kingBlackSquare == Square(fromX, fromY) && toX == 2 && toY == 7) {
                pieceBox.add(ChessPiece(3, 7, movingPiece.player, ChessPieceType.ROOK, R.drawable.br, true))
                pieceBox.remove(square(0,7))
            }
            // Проверка, рубили ли с En Passant
            if (lastMove.isNotEmpty()) {
                val lastMoveP2 = lastMove[lastMove.lastIndex]
                if (lastMoveP2.toY == 4 && lastMoveP2.fromY == 6 && square(lastMoveP2.toX, lastMoveP2.toY)?.type == ChessPieceType.PAWN &&
                        movingPiece.player == ChessPlayer.WHITE && movingPiece.y + 1 <= 7 && movingPiece.x + 1 <= 7 && movingPiece.x == lastMoveP2.toX - 1 && movingPiece.y == lastMoveP2.toY) {
                    capturedPieces.add(square(toX, toY - 1))
                    pieceBox.remove(square(toX, toY - 1))
                    enPassant = true
                }
                if (lastMoveP2.toY == 4 && lastMoveP2.fromY == 6 && square(lastMoveP2.toX, lastMoveP2.toY)?.type == ChessPieceType.PAWN &&
                        movingPiece.player == ChessPlayer.WHITE && movingPiece.y + 1 <= 7 && movingPiece.x - 1 >= 0 && movingPiece.x == lastMoveP2.toX + 1 && movingPiece.y == lastMoveP2.toY) {
                    capturedPieces.add(square(toX, toY - 1))
                    pieceBox.remove(square(toX, toY - 1))
                    enPassant = true
                }
                if (lastMoveP2.toY == 3 && lastMoveP2.fromY == 1 && square(lastMoveP2.toX, lastMoveP2.toY)?.type == ChessPieceType.PAWN &&
                        movingPiece.player == ChessPlayer.BLACK && movingPiece.y - 1 >= 0 && movingPiece.x + 1 <= 7 && movingPiece.x == lastMoveP2.toX - 1 && movingPiece.y == lastMoveP2.toY) {
                    capturedPieces.add(square(toX, toY + 1))
                    pieceBox.remove(square(toX, toY + 1))
                    enPassant = true
                }
                if (lastMoveP2.toY == 3 && lastMoveP2.fromY == 1 && square(lastMoveP2.toX, lastMoveP2.toY)?.type == ChessPieceType.PAWN &&
                        movingPiece.player == ChessPlayer.BLACK && movingPiece.y - 1 >= 0 && movingPiece.x - 1 >= 0 && movingPiece.x == lastMoveP2.toX + 1 && movingPiece.y == lastMoveP2.toY) {
                    capturedPieces.add(square(toX, toY + 1))
                    pieceBox.remove(square(toX, toY + 1))
                    enPassant = true
                }
            }
            // Проверка, дошла ли пешка до конца
            if (toY == 7 && movingPiece.type == ChessPieceType.PAWN) {
                pawnPromoted = true
                pieceBox.add(ChessPiece(toX, toY, movingPiece.player, ChessPieceType.QUEEN, R.drawable.wq, true))
            }
            if (toY == 0 && movingPiece.type == ChessPieceType.PAWN) {
                pawnPromoted = true
                pieceBox.add(ChessPiece(toX, toY, movingPiece.player, ChessPieceType.QUEEN, R.drawable.bq, true))
            }
            // Рублю фигуру
            if (Square(toX, toY) in squareUnderAttack && !enPassant) {
                Log.d(TAG, "Tried to attack")
                capturedPieces.add(square(toX, toY))
                pieceBox.remove(square(toX, toY))
            }
            // Запоминаю клетку короля
            if (Square(fromX, fromY) == kingBlackSquare) kingBlackSquare = Square(toX, toY)
            if (Square(fromX, fromY) == kingWhiteSquare) kingWhiteSquare = Square(toX, toY)
            // Передвигаю фигуру, записываю последний ход
            if (!pawnPromoted) pieceBox.add(ChessPiece(toX, toY, movingPiece.player, movingPiece.type, movingPiece.pieceType, true))
            pieceBox.remove(movingPiece)
            lastMove.add(move)
            lastMoveSuccessful = true
            whiteTurn = !whiteTurn
        }
    }

    fun previousMove() {
        if (lastMoveSuccessful) {
            val moveR = lastMove[lastMove.lastIndex]
            val toX = moveR.toX
            val toY = moveR.toY
            val fromX = moveR.fromX
            val fromY = moveR.fromY
            val movingPiece = square(toX, toY) ?: return
            // Откат если пешка -> королева
            if (pawnPromoted)
                if (movingPiece.player == ChessPlayer.WHITE) pieceBox.add(ChessPiece(fromX, fromY, movingPiece.player, ChessPieceType.PAWN, R.drawable.wp, pieceMoved))
                else pieceBox.add(ChessPiece(fromX, fromY, movingPiece.player, ChessPieceType.PAWN, R.drawable.bp, pieceMoved))
            else pieceBox.add(ChessPiece(fromX, fromY, movingPiece.player, movingPiece.type, movingPiece.pieceType, pieceMoved))
            // Просто откат
            pieceBox.remove(movingPiece)
            if (capturedPieces.isNotEmpty() && capturedPieces[capturedPieces.lastIndex]?.x == toX && capturedPieces[capturedPieces.lastIndex]?.y == toY)
                capturedPieces[capturedPieces.lastIndex]?.let { pieceBox.add(it) }
            // Откат если en passant
            if (enPassant) capturedPieces[capturedPieces.lastIndex]?.let { pieceBox.add(it) }
            // Откат если рокировка
            if (fromX == 4 && fromY == 0 && kingWhiteSquare == Square(toX, toY) && toX == 6 && toY == 0) {
                pieceBox.add(ChessPiece(7, 0, movingPiece.player, ChessPieceType.ROOK, R.drawable.wr, false))
                pieceBox.remove(square(5,0))
            }
            if (fromX == 4 && fromY == 0 && kingWhiteSquare == Square(toX, toY) && toX == 2 && toY == 0) {
                pieceBox.add(ChessPiece(0, 0, movingPiece.player, ChessPieceType.ROOK, R.drawable.wr, false))
                pieceBox.remove(square(3,0))
            }
            if (fromX == 4 && fromY == 7 && kingBlackSquare == Square(toX, toY) && toX == 6 && toY == 7) {
                pieceBox.add(ChessPiece(7, 7, movingPiece.player, ChessPieceType.ROOK, R.drawable.br, false))
                pieceBox.remove(square(5,7))
            }
            if (fromX == 4 && fromY == 7 && kingBlackSquare == Square(toX, toY) && toX == 2 && toY == 7) {
                pieceBox.add(ChessPiece(0, 7, movingPiece.player, ChessPieceType.ROOK, R.drawable.br, false))
                pieceBox.remove(square(3,7))
            }

            if (Square(toX, toY) == kingBlackSquare) kingBlackSquare = Square(fromX, fromY)
            if (Square(toX, toY) == kingWhiteSquare) kingWhiteSquare = Square(fromX, fromY)
            whiteTurn = !whiteTurn
        }
    }

    fun legalMovesForAll(moveFromPlayer: Move) {
        legalMoves.clear()
        possibleMovesForAll()
        kingUnderCheck()
        possibleMovesForAll()
        possibleMoves.forEach {
            move(it)
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