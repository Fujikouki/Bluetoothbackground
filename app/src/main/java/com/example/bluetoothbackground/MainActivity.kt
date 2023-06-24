package com.example.bluetoothbackground

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Switch
import android.view.View


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
                    val intent = Intent(this@MainActivity, bluetoothbk::class.java)

                    startService(intent)


                }

                R.id.button2 -> {
                    val intent = Intent(this@MainActivity, bluetoothbk::class.java)

                    stopService(intent)

                }
            }


        }
    }




}

