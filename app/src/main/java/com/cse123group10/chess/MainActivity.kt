package com.cse123group10.chess

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

const val debug_TAG = "MainActivity"
var checkFlag = 0
var checkMoveFlag = 0
private const val checkMessage = "King In Check"
const val duration = Toast.LENGTH_LONG

class MainActivity : AppCompatActivity(), ChessInterface {
    var boardModel = Board()
    private lateinit var boardView: ChessboardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // get size of screen for scalable baord size
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.widthPixels
        displayMetrics.heightPixels
        // draw board model in terminal
        Log.d(debug_TAG, "$boardModel")
        // create a interface so that there is only one board model to be able to pass information
        // from terminal model to screen
        boardView = findViewById<ChessboardView>(R.id.chessboard_view)
        boardView.boardInterface = this
        findViewById<Button>(R.id.Reset).setOnClickListener {
            boardModel.startUp()
            boardView.invalidate()
        }
    }

    override fun pieceAt(col: Int, row: Int): Pieces? {
        return boardModel.pieceAt(col,row)
    }

    override fun movePiece(xOrig: Int, yOrig: Int, xTo: Int, yTo: Int) {
        boardModel.movePiece(xOrig, yOrig, xTo, yTo)
        boardView.invalidate()
        if(checkFlag == 1){
            checkFlag = 0
            findViewById<TextView>(R.id.editTextTextPersonName).text = "Can't move there, King will be in check"

        }
        if(checkMoveFlag == 1){
            checkMoveFlag = 0
            findViewById<TextView>(R.id.editTextTextPersonName).text = "King is in check, move King"

        }
    }
}