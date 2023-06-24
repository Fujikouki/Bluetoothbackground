package com.example.bluetoothbackground

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class bluetoothForegroundReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val targetIntent = Intent(context, bluetoothbk::class.java)
        context.stopService(targetIntent)
    }
}