package com.cse123group10.chess

interface ChessInterface {
    fun pieceAt(col: Int, row: Int): Pieces?
    fun movePiece(xOrig: Int, yOrig: Int, xTo: Int, yTon: Int)
}