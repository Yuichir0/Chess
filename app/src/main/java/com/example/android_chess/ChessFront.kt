package com.example.android_chess

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ChessView(context: Context?, attrs: AttributeSet?): View(context, attrs) {
    private val startX = 20f
    private val startY = 180f
    private val squareSize = 130f
    private val piecesImages = setOf(
            R.drawable.bb,
            R.drawable.bh, //BH - Black Horse (the only difference from original names)
            R.drawable.bk,
            R.drawable.bp,
            R.drawable.bq,
            R.drawable.br,
            R.drawable.wb,
            R.drawable.wh,
            R.drawable.wk,
            R.drawable.wp,
            R.drawable.wq,
            R.drawable.wr
    )

    private val bitmaps = mutableMapOf<Int, Bitmap>()
    private val color = Paint()

    init {
        loadBitmap()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        createChessBoard(canvas)
        loadPieces(canvas)
    }

    private fun loadPieces(canvas: Canvas?) {
        val chessMain = ChessMain()
        chessMain.reset()
        for (row in 0..7)
            for (column in 0..7) {
                val piece = chessMain.square(column, row)
                if (piece != null)
                    loadPieceAt(canvas, column, row, piece.pieceType)
            }
    }

    private fun loadPieceAt(canvas: Canvas?, column: Int, row: Int, pieceName: Int) {
        val whiteQueenBitmap = bitmaps[pieceName]!!
        canvas?.drawBitmap(whiteQueenBitmap, null, RectF(startX + column * squareSize, startY + (7 - row) * squareSize, startX + (column + 1) * squareSize, startY + (8 - row) * squareSize), color)
    }

    private fun loadBitmap() {
        piecesImages.forEach {
            bitmaps[it] = BitmapFactory.decodeResource(resources, it)
        }
    }

    private fun createChessBoard(canvas: Canvas?) {
        color.color = Color.argb(255, 238, 238, 212)
        canvas?.drawRect(startX, startY, startX + 8 * squareSize, startY + 8 * squareSize, color)
        color.color = Color.argb(255, 125, 148, 93)
        for (i in 0..3)
            for (j in 0..3) {
                canvas?.drawRect(startX + squareSize + 2 * j * squareSize, startY + 2 * i * squareSize, startX + 2 * squareSize + 2 * j * squareSize, startY + squareSize + 2 * i * squareSize, color)
                canvas?.drawRect(startX + 2 * j * squareSize, startY + squareSize + 2 * i * squareSize, startX + squareSize + 2 * j * squareSize, startY + 2 * squareSize + 2 * i * squareSize, color)
            }
    }
}
