package com.example.bluetoothbackground

import android.app.Service
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Intent
import android.os.IBinder
import android.content.Context
import android.util.Log
import android.content.IntentFilter
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast

class bluetoothbk : Service() {

    private val REQUEST_ENABLEBLUETOOTH: Int = 1
    private val MY_REQUEST_CODE: Int = 2

    private var bluetoothAdapter: BluetoothAdapter? = null

    private var gpsEnabled: Boolean = false

    var MacAddressSet = mutableSetOf<String?>()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            //Log.d("startSuccess1", "onReceive() success")
            val action: String? = intent.action
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {

                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    if (device == null) {
                        Log.d("nullDevice", "Device is null")
                        return
                    }

                    val deviceHardwareAddress = device?.address // MAC address

                    MacAddressSet.add(deviceHardwareAddress)
                    Log.d("MacA", MacAddressSet.toString())
                    return
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Toast.makeText(
                        context,
                        "Bluetooth検出開始",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("discoveryStart", "Discovery Started")
                    return
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> { //cancelDiscoveryでも呼ばれる

                    Toast.makeText(
                        context,
                        "Bluetooth検出終了",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("discoveryFinish", "Discovery finished")
                }

            }
        }
    }


    override fun onCreate() {
        Log.d("startSuccess2", "onCreate() success")

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Log.d("bluetoothAdapterTest", "null")
            onDestroy()
            return

        }
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


        registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))

        Log.d("startSuccess3", "onCreate() success")


    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("startSuccess4", "onStartCommand() success")

        startReceiver()

        if(bluetoothAdapter!!.startDiscovery()){

            Log.d("startSuccess5", "startDiscovery() success")

        }else{
            Log.d("startSuccess6", "startDiscovery() No")
        }

        return START_NOT_STICKY

    }
    private fun startReceiver() {
        val intent = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)

    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

}