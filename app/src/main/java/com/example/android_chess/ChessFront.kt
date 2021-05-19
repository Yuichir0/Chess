package com.example.android_chess

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import java.lang.Integer.min

class ChessView(context: Context?, attrs: AttributeSet?): View(context, attrs) {
    private var startX = 20f
    private var startY = 180f
    private var squareSize = 130f
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
    var chessConnector: ChessConnector? = null
    private var startColumn: Int = -1
    private var startRow: Int = -1
    private var finishColumn: Int = -1
    private var finishRow: Int = -1

    init {
        loadBitmap()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val chessBoardSide = min(width, height) * 0.95
        squareSize = (chessBoardSide / 8).toFloat()
        startX = ((width - chessBoardSide) / 2).toFloat()
        startY = ((height - chessBoardSide) / 2).toFloat()

        createChessBoard(canvas)
        loadPieces(canvas)
    }

//    private val canvas: Canvas = TODO()
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startColumn = ((event.x - startX) / squareSize).toInt()
                startRow = 7 - ((event.y - startY) / squareSize).toInt()
                Log.d(TAG, "pressed down at ($startColumn, $startRow)")
            }
            MotionEvent.ACTION_UP -> {
                finishColumn = ((event.x - startX) / squareSize).toInt()
                finishRow = 7 - ((event.y - startY) / squareSize).toInt()
//                val chessMain = ChessMain()
                chessConnector?.movePiece(startColumn, startRow, finishColumn, finishRow)
//                chessMain.square(finishColumn, finishRow)?.let { loadPieceAt(canvas, finishColumn, finishRow, it.pieceType) }
                Log.d(TAG, "pressed up at ($finishColumn, $finishRow)")
            }
        }
        return true
    }

    private fun loadPieces(canvas: Canvas) {
        val chessMain = ChessMain()
        chessMain.reset()
        for (row in 0..7)
            for (column in 0..7) {
                val piece = chessConnector?.square(column, row)
                if (piece != null)
                    loadPieceAt(canvas, column, row, piece.pieceType)
            }
    }

    private fun loadPieceAt(canvas: Canvas, column: Int, row: Int, pieceName: Int) {
        val loadingPiece = bitmaps[pieceName]!!
        canvas.drawBitmap(loadingPiece, null, RectF(startX + column * squareSize, startY + (7 - row) * squareSize, startX + (column + 1) * squareSize, startY + (8 - row) * squareSize), color)
    }

    private fun loadBitmap() {
        piecesImages.forEach {
            bitmaps[it] = BitmapFactory.decodeResource(resources, it)
        }
    }

    private fun createChessBoard(canvas: Canvas) {
        color.color = Color.argb(255, 238, 238, 212)
        canvas.drawRect(startX, startY, startX + 8 * squareSize, startY + 8 * squareSize, color)
        color.color = Color.argb(255, 125, 148, 93)
        for (i in 0..3)
            for (j in 0..3) {
                canvas.drawRect(startX + squareSize + 2 * j * squareSize, startY + 2 * i * squareSize, startX + 2 * squareSize + 2 * j * squareSize, startY + squareSize + 2 * i * squareSize, color)
                canvas.drawRect(startX + 2 * j * squareSize, startY + squareSize + 2 * i * squareSize, startX + squareSize + 2 * j * squareSize, startY + 2 * squareSize + 2 * i * squareSize, color)
            }
    }
}
