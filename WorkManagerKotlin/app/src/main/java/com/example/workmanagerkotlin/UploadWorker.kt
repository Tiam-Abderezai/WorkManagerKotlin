package com.example.workmanagerkotlin

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.telephony.SmsManager
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

class UploadWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {


        override fun doWork(): Result {

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())


            SmsManager.getDefault()
                .sendTextMessage("+19494564821", null, "Date: " + currentDate, null, null)
            println("DATE "+currentDate)
            return Result.success()
        }

    }

