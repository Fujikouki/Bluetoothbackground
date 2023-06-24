package com.example.bluetoothbackground

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.view.View
import android.content.Context
import android.util.Log


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val listener = ScanListener()

        var scanButton = findViewById<Button>(R.id.button)
        scanButton.setOnClickListener(listener)

        var cancelButton = findViewById<Button>(R.id.button2)
        cancelButton.setOnClickListener(listener)


    }

    private inner class ScanListener : View.OnClickListener {
        override fun onClick(view: View) {

            when (view.id) {
                R.id.button -> {
                    Log.d("button1", "startOn")

                    val intent = Intent(this@MainActivity,bluetoothbk::class.java)
                    startForegroundService(intent);


                }

                R.id.button2 -> {
                    Log.d("button2", "cancle")
                    val intent = Intent(this@MainActivity,bluetoothbk::class.java)
                    stopService(intent);



                }
            }


        }
    }





}

