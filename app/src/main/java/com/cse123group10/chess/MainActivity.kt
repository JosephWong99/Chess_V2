package com.cse123group10.chess

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

private const val debug_TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    var boardModel = Board()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(debug_TAG, "$boardModel")
    }
}