package com.cse123group10.chess

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

const val debug_TAG = "MainActivity"

class MainActivity : AppCompatActivity(), ChessInterface {

    var boardModel = Board()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // get size of screen for scalable baord size
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels
        // draw board model in terminal
        Log.d(debug_TAG, "$boardModel")
        // create a interface so that there is only one board model to be able to pass information
        // from terminal model to screen
        findViewById<ChessboardView>(R.id.chessboard_view).boardInterface = this
    }

    override fun pieceAt(col: Int, row: Int): Pieces? {
        return boardModel.pieceAt(col,row)
    }

    override fun movePiece(xOrig: Int, yOrig: Int, xTo: Int, yTo: Int) {
        boardModel.movePiece(xOrig, yOrig, xTo, yTo)
        findViewById<ChessboardView>(R.id.chessboard_view).invalidate()
    }
}