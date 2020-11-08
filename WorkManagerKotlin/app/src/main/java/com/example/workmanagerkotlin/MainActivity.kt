package com.example.workmanagerkotlin

import android.app.PendingIntent
import android.content.BroadcastReceiver
import  android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.BatteryManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import javax.*
import androidx.work.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // When initialized, schedules a worker task to send
        // SMS of the local time every 15 mins to the number specified
        // by the user in the first parameter of the SmsManager.getDefault()
        // .sendTextMessage() method inside sendMessage() function
        // located at the bottom of this class
        val constraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        val uploadWorkRequest = PeriodicWorkRequest.Builder(
            UploadWorker::class.java,
            15,
            TimeUnit.MINUTES
        )
            .setConstraints(constraint)
            .addTag("my_unique_worker")
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "my_unique_worker",
                ExistingPeriodicWorkPolicy.KEEP,
                uploadWorkRequest
            )

// Confirms user permission for SMS before sending/receiving SMS

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.RECEIVE_SMS,
                    android.Manifest.permission.SEND_SMS
                ),
                111
            )
        } else
            receiveMsg()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendMessage()
        }
    }

    private fun receiveMsg() {
        var br = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {


                    for (sms in Telephony.Sms.Intents.getMessagesFromIntent(p1)) {

                        Toast.makeText(
                            applicationContext,
                            sms.displayMessageBody,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }

    private fun sendMessage() {

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())


        SmsManager.getDefault()
            .sendTextMessage(
                // Enter phone number as the first
                // parameter in String format.
                // for example "+13105551234"
                "+13105551234",
                null,
                "Date: " + currentDate,
                null,
                null
            )
    }

}