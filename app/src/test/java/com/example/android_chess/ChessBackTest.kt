package com.example.android_chess

import org.junit.Assert.*
import org.junit.Test

class ChessBackTest {
    @Test
    fun cannotMoveThroughOtherPieces() {
        val chessBack = ChessBack()
        var movingPiece = chessBack.square(0,0)
        chessBack.moveFromPlayer(Move(0,0,0,2)) // Попытка сходить ладьей через фигуру
        assertNotEquals(movingPiece?.type, chessBack.square(0,2)?.type)

        movingPiece = chessBack.square(1,0)
        chessBack.moveFromPlayer(Move(1,0, 2,2)) // Ход конем через фигуру
        assertEquals(movingPiece?.type, chessBack.square(2,2)?.type)
    }

    @Test
    fun cannotMoveOnYourPieces() {
        val chessBack = ChessBack()
        val movingPiece = chessBack.square(0,0)
        chessBack.moveFromPlayer(Move(0,0,0,1)) // Ход ладьей на свою пешку
        assertNotEquals(movingPiece?.type, chessBack.square(0,1)?.type)
    }

    @Test
    fun canEatOpponentPieces() {
        val chessBack = ChessBack()
        chessBack.moveFromPlayer(Move(4,1,4,3)) // Ход пешкой на E4
        chessBack.moveFromPlayer(Move(3,6,3,4)) // Ход пешкой на D5

        val movingPiece = chessBack.square(4,3)
        chessBack.moveFromPlayer(Move(4,3,3,4)) // Атака пешкой с E4 на D5
        assertEquals(movingPiece?.player, chessBack.square(3,4)?.player) // Проверка, что на D5 осталась белая пешка
    }

    @Test
    fun moveKing() {
        val chessBack = ChessBack()
        chessBack.moveFromPlayer(Move(4,1,4,3)) // Ход пешкой на E4
        chessBack.moveFromPlayer(Move(3,6,3,4)) // Ход пешкой на D5

        var movingPiece = chessBack.square(4,0)
        chessBack.moveFromPlayer(Move(4,0,4,1)) // Ход королем белых по вертикали
        assertEquals(movingPiece?.type, chessBack.square(4,1)?.type) // Проверка, что король сходил

        movingPiece = chessBack.square(4,7)
        chessBack.moveFromPlayer(Move(4,7,3,6)) // Ход королем черных по диагонали
        assertEquals(movingPiece?.type, chessBack.square(3,6)?.type) // Проверка, что король сходил

        chessBack.moveFromPlayer(Move(4,1,4,2)) // Хожу королями, чтобы проверить ход по горизонтали
        chessBack.moveFromPlayer(Move(3,6,3,5))

        movingPiece = chessBack.square(4,2)
        chessBack.moveFromPlayer(Move(4,2,5,2)) // Ход королем белых по горизонтали
        assertEquals(movingPiece?.type, chessBack.square(5,2)?.type) // Проверка, что король сходил
    }

    @Test
    fun moveQueen() {
        val chessBack = ChessBack()
        chessBack.moveFromPlayer(Move(4,1,4,3)) // Ход пешкой на E4
        chessBack.moveFromPlayer(Move(3,6,3,4)) // Ход пешкой на D5

        var movingPiece = chessBack.square(3,0)
        chessBack.moveFromPlayer(Move(3,0,6,3)) // Ход королевой белых по диагонали на 3 клетки
        assertEquals(movingPiece?.type, chessBack.square(6,3)?.type) // Проверка, что королева сходил

        movingPiece = chessBack.square(3,7)
        chessBack.moveFromPlayer(Move(3,7,3,5)) // Ход королевой черных по вертикали на 2 клетки
        assertEquals(movingPiece?.type, chessBack.square(3,5)?.type) // Проверка, что королева сходил

        movingPiece = chessBack.square(6,3)
        chessBack.moveFromPlayer(Move(6,3,5,3)) // Ход королевой белых по горизонтали на 1 клетку
        assertEquals(movingPiece?.type, chessBack.square(5,3)?.type) // Проверка, что королева сходила
    }

    @Test
    fun moveRook() {
        val chessBack = ChessBack()
        chessBack.moveFromPlayer(Move(7,1,7,3)) // Ход пешкой на H4
        chessBack.moveFromPlayer(Move(0,6,0,4)) // Ход пешкой на A5

        var movingPiece = chessBack.square(7,0)
        chessBack.moveFromPlayer(Move(7,0,7,2)) // Ход белой ладьей по вертикали на 2 клетки
        assertEquals(movingPiece?.type, chessBack.square(7,2)?.type) // Проверка, что ладья сходила

        movingPiece = chessBack.square(0,7)
        chessBack.moveFromPlayer(Move(0,7,0,6)) // Ход черной ладьей по вертикали на 1 клетку
        assertEquals(movingPiece?.type, chessBack.square(0,6)?.type) // Проверка, что ладья сходила

        movingPiece = chessBack.square(7,2)
        chessBack.moveFromPlayer(Move(7,2,0,2)) // Ход белой ладьей по горизонтали на 7 клеток
        assertEquals(movingPiece?.type, chessBack.square(0,2)?.type) // Проверка, что ладья сходила
    }

    @Test
    fun moveBishop() {
        val chessBack = ChessBack()
        chessBack.moveFromPlayer(Move(4,1,4,3)) // Ход пешкой на E4
        chessBack.moveFromPlayer(Move(3,6,3,4)) // Ход пешкой на D5

        var movingPiece = chessBack.square(5,0)
        chessBack.moveFromPlayer(Move(5,0,4,1)) // Ход белым слоном по вертикали на 1 клетку
        assertEquals(movingPiece?.type, chessBack.square(4,1)?.type) // Проверка, что слон сходил

        movingPiece = chessBack.square(2,7)
        chessBack.moveFromPlayer(Move(2,7,7,2)) // Ход черным слоном по вертикали на 5 клеток
        assertEquals(movingPiece?.type, chessBack.square(7,2)?.type) // Проверка, что слон сходил
    }

    @Test
    fun moveKnight() {
        val chessBack = ChessBack()
        var movingPiece = chessBack.square(1,0)
        chessBack.moveFromPlayer(Move(1,0,2,2)) // Прыжок белым конем на C3 (1 горизонталь 2 вертикаль)
        assertEquals(movingPiece?.type, chessBack.square(2,2)?.type) // Проверка, что конь прыгнул

        movingPiece = chessBack.square(1,7)
        chessBack.moveFromPlayer(Move(1,7,2,5)) // Прыжок белым конем на C6 (1 горизонталь 2 вертикаль)
        assertEquals(movingPiece?.type, chessBack.square(2,5)?.type) // Проверка, что конь прыгнул

        movingPiece = chessBack.square(2,2)
        chessBack.moveFromPlayer(Move(2,2,4,3)) // Прыжок белым конем на E4 (2 горизонталь 1 вертикаль)
        assertEquals(movingPiece?.type, chessBack.square(4,3)?.type) // Проверка, что конь прыгнул
    }

    @Test
    fun movePawn() {
        val chessBack = ChessBack()
        var movingPiece = chessBack.square(4,1)
        chessBack.moveFromPlayer(Move(4,1,4,3)) // Ход пешкой на 2 клетки E4
        assertEquals(movingPiece?.type, chessBack.square(4,3)?.type) // Проверка, что пешка сходила

        movingPiece = chessBack.square(3,6)
        chessBack.moveFromPlayer(Move(3,6,3,5)) // Ход пешкой на 1 клетку D6
        assertEquals(movingPiece?.type, chessBack.square(3,5)?.type) // Проверка, что пешка сходила

        chessBack.moveFromPlayer(Move(4,3,4,4)) // E5

        movingPiece = chessBack.square(3,5)
        chessBack.moveFromPlayer(Move(3,5,4,4)) // Атака пешкой с D6 пешки на E5
        assertEquals(movingPiece?.player, chessBack.square(4,4)?.player) // Проверка, что пешка срубила по диагонали на 1

        chessBack.moveFromPlayer(Move(0,1,0,2)) // Случайные ходы, чтобы довести черную пешку до ряда 0
        chessBack.moveFromPlayer(Move(4,4,4,3))
        chessBack.moveFromPlayer(Move(5,1,5,2))
        chessBack.moveFromPlayer(Move(4,3,5,2))
        chessBack.moveFromPlayer(Move(0,2,0,3))
        chessBack.moveFromPlayer(Move(5,2,6,1))
        chessBack.moveFromPlayer(Move(0,3,0,4))

        chessBack.moveFromPlayer(Move(6,1,7,0)) // Пешка ходит на H1 и превращается в королеву
        assertEquals(ChessPieceType.QUEEN, chessBack.square(7,0)?.type) // Проверка, что пешка повысилась
        assertEquals(ChessPlayer.BLACK, chessBack.square(7,0)?.player)
    }

    @Test
    fun canCastle() {
        val chessBack = ChessBack()
        chessBack.moveFromPlayer(Move(6,0,5,2)) // Делаю ходы, чтобы освободить клетки между королем и ладьей
        chessBack.moveFromPlayer(Move(6,7,7,5))
        chessBack.moveFromPlayer(Move(6,1,6,2))
        chessBack.moveFromPlayer(Move(6,6,6,5))
        chessBack.moveFromPlayer(Move(5,0,6,1))
        chessBack.moveFromPlayer(Move(5,7,6,6))

        val movingPiece = chessBack.square(4,0)
        chessBack.moveFromPlayer(Move(4,0,6,0)) // Короткая рокировка белого короля
        assertEquals(movingPiece?.type, chessBack.square(6,0)?.type) // Проверка что король дошел
        assertEquals(ChessPieceType.ROOK, chessBack.square(5,0)?.type) // Проверка что ладья дошла
    }

    @Test
    fun canEnPassant() {
        val chessBack = ChessBack()
        chessBack.moveFromPlayer(Move(4, 1, 4, 3)) // Ход пешкой на E4
        chessBack.moveFromPlayer(Move(3, 6, 3, 4)) // Ход пешкой на D5

        chessBack.moveFromPlayer(Move(4, 3, 3, 4)) // Атака пешкой с E4 на D5
        chessBack.moveFromPlayer(Move(4, 6, 4,4)) // Ход пешкой на E5

        val movingPiece = chessBack.square(3, 4)
        chessBack.moveFromPlayer(Move(3, 4, 4, 5)) // en passant
        assertEquals(movingPiece?.type, chessBack.square(4, 5)?.type) // Проверка что пешка оказалась на клетке, куда била
        assertEquals(null, chessBack.square(4, 4)?.player) // Проверка что черная пешка съелась
    }
}