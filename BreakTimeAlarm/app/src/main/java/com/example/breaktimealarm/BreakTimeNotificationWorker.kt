package com.example.breaktimealarm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf


const val FINISH = "finish"

class BreakTimeNotificationWorker(context: Context, params: WorkerParameters): Worker(context, params) {

    private val notificationId = 1

    override fun doWork(): Result {

        return try {
            val inputData = inputData.getBoolean(FINISH, true)

            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent = PendingIntent
                .getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(applicationContext, MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(applicationContext.getString(R.string.break_time_notification_title))
                .setContentText(applicationContext.getText(R.string.break_time_notification_description))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(applicationContext)){
               notify(notificationId, builder.build())
            }

            Log.d("worker", "create notification")
            val data = workDataOf(FINISH to false)
            Result.success(data)
        }catch (e:Exception){
            Log.d("BreakTimeAlarm", "failure : $e")
            Result.failure()
        }
    }
}