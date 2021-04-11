package com.cse123group10.chess

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.PrintWriter
import java.net.ConnectException
import java.net.Socket
import java.util.*
import java.util.concurrent.Executors

const val debug_TAG = "MainActivity"
var checkFlag = 0
var checkMoveFlag = 0
private const val checkMessage = "King In Check"
const val duration = Toast.LENGTH_LONG

class MainActivity : AppCompatActivity(), ChessInterface {
    var boardModel = Board()
    private lateinit var boardView: ChessboardView
    private lateinit var connectButton: Button
    private lateinit var socket: Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // get size of screen for scalable board size
        val displayMetrics = DisplayMetrics()
        displayMetrics.widthPixels
        displayMetrics.heightPixels
        // draw board model in terminal
        Log.d(debug_TAG, "$boardModel")
        // create a interface so that there is only one board model to be able to pass information
        // from terminal model to screen
        boardView = findViewById<ChessboardView>(R.id.chessboard_view)
        boardView.boardInterface = this
        findViewById<Button>(R.id.Reset).setOnClickListener {
            Log.d(debug_TAG, "reset pressed")
            boardModel.startUp()
            boardView.invalidate()
        }
        connectButton = findViewById<Button>(R.id.Connect)
        connectButton.setOnClickListener {
            Executors.newSingleThreadExecutor().execute {
                try {
                    // Host should be personal computer IP address and port should be server port
                    socket = Socket("192.168.1.78", 10000)// 10.0.0.1 or 127.0.0.1 or 10.0.0.18
                    Log.d(debug_TAG, "pressed")
                    val scanner = Scanner(socket.getInputStream())
                    val printWriter = PrintWriter(socket.getOutputStream(), true)
                    //Log.d(debug_TAG, "${scanner.nextLine()}")
                    Log.d(debug_TAG, "before hasnext() testing")
                    if (scanner.hasNext()) {
                        Log.d(debug_TAG, "can read")
                    } else {
                        Log.d(debug_TAG, "cant read")
                    }
                    Log.d(debug_TAG, "after hasnext() testing")
                    while (scanner.hasNext()) {
                        Log.d(debug_TAG, "get string")
                        val moveString = scanner.next()
                        Log.d(debug_TAG, "splitting")
                        val move: List<Int> = moveString.split(",").map { it.toInt() }
                        Log.d(debug_TAG, "String has been split")
                        runOnUiThread {
                            Log.d(debug_TAG, "move pice")
                            movePiece(move[0], move[1], move[2], move[3])
                            boardView.invalidate()
                        }
                    }
                }catch(e: ConnectException){
                    Log.d(debug_TAG, "failed connect")
                }
            }
        }
        findViewById<Button>(R.id.Host).setOnClickListener {

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