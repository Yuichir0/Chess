package com.example.android_chess

class ChessMain {
    var pieceBox = mutableSetOf<ChessPiece>()

    init {
        reset()
    }

    fun movePiece(startColumn: Int, startRow: Int, finishColumn: Int, finishRow: Int) {
        val movingPiece = square(startColumn, startRow) ?: return
        square(finishColumn, finishRow)?.let {
            if (it.player == movingPiece.player) return
            pieceBox.remove(it)
        }

        if (finishColumn < 0 || finishColumn > 7 || finishRow < 0 || finishRow > 7) return
        pieceBox.remove(movingPiece)
        pieceBox.add(ChessPiece(finishColumn, finishRow, movingPiece.player, movingPiece.type, movingPiece.pieceType))
    }

    fun reset() {
        pieceBox.removeAll(pieceBox)
        for (i in 0..7) pieceBox.add(ChessPiece(i,1, ChessPlayer.WHITE, ChessPieceType.PAWN, R.drawable.wp))
        for (i in 0..7) pieceBox.add(ChessPiece(i,6, ChessPlayer.BLACK, ChessPieceType.PAWN, R.drawable.bp))
        for (i in 0..1) pieceBox.add(ChessPiece(0 + 7 * i,0, ChessPlayer.WHITE, ChessPieceType.ROOK, R.drawable.wr))
        for (i in 0..1) pieceBox.add(ChessPiece(0 + 7 * i,7, ChessPlayer.BLACK, ChessPieceType.ROOK, R.drawable.br))
        for (i in 0..1) pieceBox.add(ChessPiece(1 + 5 * i,0, ChessPlayer.WHITE, ChessPieceType.KNIGHT, R.drawable.wh))
        for (i in 0..1) pieceBox.add(ChessPiece(1 + 5 * i,7, ChessPlayer.BLACK, ChessPieceType.KNIGHT, R.drawable.bh))
        for (i in 0..1) pieceBox.add(ChessPiece(2 + 3 * i,0, ChessPlayer.WHITE, ChessPieceType.BISHOP, R.drawable.wb))
        for (i in 0..1) pieceBox.add(ChessPiece(2 + 3 * i,7, ChessPlayer.BLACK, ChessPieceType.BISHOP, R.drawable.bb))
        pieceBox.add(ChessPiece(3,0, ChessPlayer.WHITE, ChessPieceType.QUEEN, R.drawable.wq))
        pieceBox.add(ChessPiece(3,7, ChessPlayer.BLACK, ChessPieceType.QUEEN, R.drawable.bq))
        pieceBox.add(ChessPiece(4,0, ChessPlayer.WHITE, ChessPieceType.KING, R.drawable.wk))
        pieceBox.add(ChessPiece(4,7, ChessPlayer.BLACK, ChessPieceType.KING, R.drawable.bk))

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