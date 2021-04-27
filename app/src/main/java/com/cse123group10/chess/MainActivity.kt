package com.cse123group10.chess

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Button
import android.widget.EditText
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
    private lateinit var editText: EditText
    private var printWriter: PrintWriter? = null
    private val ack_msg = "ACK                                                                        "
    var app_move = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // get size of screen for scalable board size
        val displayMetrics = DisplayMetrics()
        displayMetrics.widthPixels
        displayMetrics.heightPixels
        // draw board model in terminal
        //Log.d(debug_TAG, "$boardModel")
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
        editText = findViewById<EditText>(R.id.editTextTextPersonName)
        connectButton.setOnClickListener {
            Executors.newSingleThreadExecutor().execute {
                try {
                    // Host should be personal computer IP address and port should be server port
                    var text = editText.text.toString()
                    var ip: List<String> = text.split(":")
                    socket = Socket("${ip[0]}", ip[1].toInt())// 10.0.0.1 or 127.0.0.1 or 10.0.0.18
                    var scanner = Scanner(socket.getInputStream())
                    if (printWriter == null) {
                        printWriter = PrintWriter(socket.getOutputStream())
                    }
                    printWriter!!.println("Group 10 CAPSTONE CHESS")
                    printWriter!!.flush()
                    printWriter!!.println("New_game: True Color: White Saved_Game_Number: 0")
                    this.printWriter!!.flush()
                        while (scanner.hasNext()) {
                            Log.d(debug_TAG,"SCANNER READING FROM SOCKET")
                            //scanner.nextLine()
                            val moveString = scanner.next()
                            val moveStrings = scanner.next()
                            val coords = scanner.next()
                            scanner.next()
                            scanner.next()
                            scanner.next()
                            scanner.next()
                            Log.d(debug_TAG, "RECIEVED MSG:")
                            Log.d(debug_TAG, moveString)
                            Log.d(debug_TAG, moveStrings)
                            val move = coords.split("(")
                            val origX = move[1].split(",")
                            val origY = origX[1].split(")")
                            val toX = move[2].split(",")
                            val toY = toX[1].split(")")
                            app_move = false
                            runOnUiThread {
                                Executors.newSingleThreadExecutor().execute {
                                    printWriter!!.println(ack_msg)
                                    printWriter!!.flush()
                                }
                                movePiece(origX[0].toInt(), origY[0].toInt(), toX[0].toInt(), toY[0].toInt())
                                boardView.invalidate()
                            }

                    }
                }catch(e: ConnectException){
                    Log.d(debug_TAG, "failed connect")
                    findViewById<TextView>(R.id.editTextTextPersonName).text = "connection failed, input should be: ip:port"
                }
            }
        }
        findViewById<Button>(R.id.Host).setOnClickListener {

        }
    }

    override fun pieceAt(col: Int, row: Int): Pieces? {
        return boardModel.pieceAt(col,row)
    }

    override fun movePiece(xOrig: Int, yOrig: Int, xTo: Int, yTon: Int) {
        boardModel.movePiece(xOrig, yOrig, xTo, yTon)
        boardView.invalidate()
//        if(checkFlag == 1){
//            checkFlag = 0
//            findViewById<TextView>(R.id.editTextTextPersonName).text = "Can't move there, King will be in check"
//
//        }
//        if(checkMoveFlag == 1){
//            findViewById<TextView>(R.id.editTextTextPersonName).text = "King is in check, move King"
//        }
        // Coordinates: (x_0, y_0) (x_1,y_1) Check: True/False Checkmate: True/False
            val moveStr : String =
                "Coordinates: (${xOrig},${yOrig})(${xTo},${yTon}) Check: false Checkmate: False                      "
        if(app_move) {
            printWriter.let {
                Executors.newSingleThreadExecutor().execute {
                    Log.d(debug_TAG, moveStr)
//                    if(!first) {
//                        it?.println(ack_msg)
//                    }
                    it?.println(moveStr)
                    it?.flush()
                }
            }
        }else{
            app_move = true
        }
    }
}