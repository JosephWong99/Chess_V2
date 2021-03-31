package com.cse123group10.chess

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ChessboardView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    // setup variables used for sizing of board
    private final val paint = Paint()
    private final val lengthSquare: Int
        get() = (width-20)/8
    private final val xStart = 10f
    private final val yStart = 250f
    private final val xStartInt = 10
    private final val yStartInt = 250
    // images taken from
    // https://commons.wikimedia.org/wiki/Category:SVG_chess_pieces
    private val imgIDs = setOf(
        R.drawable.blackking,
        R.drawable.blackbishop,
        R.drawable.blackknight,
        R.drawable.blackpawn,
        R.drawable.blackqueen,
        R.drawable.blackrook,
        R.drawable.whiteking,
        R.drawable.whitebishop,
        R.drawable.whiteknight,
        R.drawable.whitepawn,
        R.drawable.whitequeen,
        R.drawable.whiterook,
    )
    private val bitmaps = mutableMapOf<Int, Bitmap>()
    // run on start up
    init{
        loadBitMaps()
    }
    override fun onDraw(canvas: Canvas?){
        drawBoard(canvas)
        drawPieces(canvas)
    }
    // draw chess board
    private fun drawBoard(canvas: Canvas?){
        for(j in 0..3) {
            for (i in 0..3) {
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 5f
                paint.color = Color.DKGRAY
                // draws the row of white squares that start at the edge (8,6,4,2)
                canvas?.drawRect(
                    xStart + 2 * i * lengthSquare,
                    yStart + 2 * j * lengthSquare ,
                    (xStart + 2 * i * lengthSquare) + lengthSquare,
                    (yStart + 2 * j * lengthSquare) + lengthSquare,
                    paint
                )
                // draws the white squares that are after a black square (7,5,3,1)
                canvas?.drawRect(
                    (xStart + lengthSquare) + 2 * i * lengthSquare,
                    (yStart + lengthSquare) + 2 * j * lengthSquare,
                    ((xStart + lengthSquare) + 2 * i * lengthSquare) + lengthSquare,
                    ((yStart + lengthSquare) + 2 * j * lengthSquare)+ lengthSquare,
                    paint
                )
                paint.style = Paint.Style.FILL_AND_STROKE
                paint.color = Color.DKGRAY
                paint.strokeWidth = 5f
                // draws the black square for rows 8,6,4,2
                canvas?.drawRect(
                    xStart + (2 * i + 1) * lengthSquare,
                    yStart + 2 * j * lengthSquare,
                    xStart + (2 * i + 2) * lengthSquare,
                    (yStart+lengthSquare) + 2 * j * lengthSquare,
                    paint
                )
                // draws the black square for rows 7,5,3,1
                canvas?.drawRect(
                    xStart + (2 * i) * lengthSquare,
                    (yStart + lengthSquare) + 2 * j * lengthSquare,
                    xStart + (2 * i + 1) * lengthSquare,
                    ((yStart + lengthSquare) + 2 * j * lengthSquare)+ lengthSquare,
                    paint
                )
            }
        }
    }
    // load all BitMaps to be used
    private fun loadBitMaps(){
        imgIDs.forEach{
            bitmaps[it] = BitmapFactory.decodeResource(resources,it)
        }
    }
    // draw each piece in inital places
    private fun drawPieces(canvas: Canvas?){
        val boardModel = Board()
        boardModel.startUp()
        for(j in 0..7){
            for (i in 0..7){
                val piece = boardModel.pieceAt(i,j)
                if (piece != null){
                    drawPieceAt(canvas, i, j, piece.id)
                }
            }
        }
    }
    //draws a piece at a certain location
    private fun drawPieceAt(canvas: Canvas?, col: Int, row: Int, piece: Int){
        val bitmap = bitmaps[piece]!!
        canvas?.drawBitmap(bitmap, null, RectF(xStart+col*lengthSquare,yStart+row*lengthSquare,xStart+(col+1)*lengthSquare,yStart+(row+1)*lengthSquare), paint)
    }
    // this function assumes that the move is valid and will not do any error checking
    private fun movePiece(){

    }
}