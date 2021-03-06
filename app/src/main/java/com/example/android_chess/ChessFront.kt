package com.example.android_chess

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import java.lang.Integer.min

class ChessFront(context: Context?, attrs: AttributeSet?): View(context, attrs) {
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
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val min = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(min, min)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onDraw(canvas: Canvas) {
        val chessBoardSide = min(width, height) * 0.95
        squareSize = (chessBoardSide / 8).toFloat()
        startX = ((width - chessBoardSide) / 2).toFloat()
        startY = ((height - chessBoardSide) / 2).toFloat()

        createChessBoard(canvas)
        loadPieces(canvas)
    }

    var clickedOnce = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false

        when (event.action) {
            MotionEvent.ACTION_UP -> {
                if (!clickedOnce) {
                    startColumn = ((event.x - startX) / squareSize).toInt()
                    startRow = 7 - ((event.y - startY) / squareSize).toInt()
                    if (chessConnector?.square(startColumn, startRow) != null)
                        clickedOnce = true
                } else {
                    finishColumn = ((event.x - startX) / squareSize).toInt()
                    finishRow = 7 - ((event.y - startY) / squareSize).toInt()
                    chessConnector?.moveFromPlayer(Move(startColumn, startRow, finishColumn, finishRow))
                    clickedOnce = false
                }
            }
        }
        return true
    }

    private fun loadPieces(canvas: Canvas) {
        for (row in 0..7)
            for (column in 0..7) {
                val piece = chessConnector?.square(column, row)
                if (piece != null) {
                    val white = piece.player == ChessPlayer.WHITE
                    val frontPieceType: Int = when (piece.type) {
                        ChessPieceType.KING -> if (white) R.drawable.wk else R.drawable.bk
                        ChessPieceType.QUEEN -> if (white) R.drawable.wq else R.drawable.bq
                        ChessPieceType.ROOK -> if (white) R.drawable.wr else R.drawable.br
                        ChessPieceType.BISHOP -> if (white) R.drawable.wb else R.drawable.bb
                        ChessPieceType.KNIGHT -> if (white) R.drawable.wh else R.drawable.bh
                        ChessPieceType.PAWN -> if (white) R.drawable.wp else R.drawable.bp
                    }
                    loadPieceAt(canvas, column, row, frontPieceType)
                }
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