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
                    val intent = Intent(this@MainActivity, bluetoothbk::class.java)
                    val requestCode = 1
                    val pendingIntent = PendingIntent.getService(this@MainActivity, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)

                    //startService(intent)

                    val manager = this@MainActivity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    manager.setRepeating(AlarmManager.RTC_WAKEUP, 0,30000, pendingIntent)

                }

                R.id.button2 -> {
                    Log.d("button2", "cancle")
                    val intent = Intent(this@MainActivity, bluetoothbk::class.java)
                    val requestCode = 1
                    val pendingIntent = PendingIntent.getService(this@MainActivity, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    val manager = this@MainActivity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    manager.cancel(pendingIntent)


                }
            }


        }
    }





}

