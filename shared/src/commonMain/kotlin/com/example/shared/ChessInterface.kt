package com.example.shared

interface ChessInterface {
    fun pieceAt(col: Int, row: Int): Pieces?
    fun movePiece(xOrig: Int, yOrig: Int, xTo: Int, yTo: Int)
}