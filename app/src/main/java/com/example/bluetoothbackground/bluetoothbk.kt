package com.example.bluetoothbackground

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import androidx.core.app.NotificationCompat
import android.app.AlarmManager
class bluetoothbk : Service() {
    companion object {
        const val CHANNEL_ID = "1111"
    }

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
        var locationManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


        registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))

        Log.d("startSuccess3", "onCreate() success")




    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //1．通知領域タップで戻ってくる先のActivity
        val openIntent = Intent(this, MainActivity::class.java).let {
            PendingIntent.getActivity(this, 0, it, 0)
        }
        //2．通知チャネル登録
        val channelId = CHANNEL_ID
        val channelName = "TestService Channel"
        val channel = NotificationChannel(
            channelId, channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
        //3．ブロードキャストレシーバーをPendingIntent化
        val sendIntent = Intent(this, bluetoothbk::class.java).apply {
            action = Intent.ACTION_SEND
        }
        val sendPendingIntent = PendingIntent.getBroadcast(this, 0, sendIntent, 0)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("フォアグラウンドのテスト中")
            .setContentText("終了する場合はこちらから行って下さい。")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(openIntent)
            .addAction(R.drawable.ic_launcher_foreground, "実行終了", sendPendingIntent)
            .build()

        //5．フォアグラウンド開始。
        startForeground(2222, notification)


        Log.d("startSuccess4", "onStartCommand() success")

        startReceiver()

        if(bluetoothAdapter!!.startDiscovery()){


            Log.d("startSuccess5", "startDiscovery() success")

        }else{
            Log.d("startSuccess6", "startDiscovery() No")
        }

        setNextAlarmService(this)

        return START_NOT_STICKY

    }
    // 次のアラームの設定
    private fun setNextAlarmService(context: Context) {

        // 15分毎のアラーム設定
        val repeatPeriod: Long = 1 * 60 * 1000

        val intent = Intent(context, bluetoothbk::class.java)

        val startMillis = System.currentTimeMillis() + repeatPeriod

        val pendingIntent = PendingIntent.getService(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Android Oreo 以上を想定
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            startMillis,
            pendingIntent
        )
    }
    private fun stopAlarmService() {
        val intent = Intent(this, bluetoothbk::class.java)
        val pendingIntent = PendingIntent.getService(this, 0, intent, 0)

        // アラームを解除する
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        alarmManager?.cancel(pendingIntent)
    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
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