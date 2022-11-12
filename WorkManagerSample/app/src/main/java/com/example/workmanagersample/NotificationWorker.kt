package com.example.workmanagersample

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, params: WorkerParameters):Worker(context, params) {

    private val notificationId = 1

    override fun doWork(): Result {
        return try {
            val inputBreakTime = inputData.getLong(MINUTE, 0)

            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent = PendingIntent
                .getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(applicationContext, MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Notification")
                .setContentText(applicationContext.getString(R.string.break_time_notification_description, inputBreakTime.toInt()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(applicationContext)){
                notify(notificationId, builder.build())
            }

            Result.success()
        }catch (e:Exception){
            Log.d("BreakTimeAlarm", "failure : $e")
            Result.failure()
        }
    }
}