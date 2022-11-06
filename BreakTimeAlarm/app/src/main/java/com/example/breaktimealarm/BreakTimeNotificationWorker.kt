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
import java.text.SimpleDateFormat
import java.util.*


const val IS_RESTING = "isResting"
const val BREAK_TIME = "break time"
const val LAST_BREAK_TIME = "last break time"

class BreakTimeNotificationWorker(context: Context, params: WorkerParameters): Worker(context, params) {

    private val notificationId = 1

    override fun doWork(): Result {

        return try {
            val inputBreakTime = inputData.getLong(BREAK_TIME, 0)

            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val pendingIntent = PendingIntent
                .getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(applicationContext, MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(applicationContext.getString(R.string.break_time_notification_title))
                .setContentText(applicationContext.getString(R.string.break_time_notification_description, inputBreakTime.toInt()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(applicationContext)){
               notify(notificationId, builder.build())
            }

            Log.d("worker", "create notification")
            val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPAN).format(Date())
            val data = workDataOf(
                IS_RESTING to false,
                LAST_BREAK_TIME to currentDate
            )
            Result.success(data)
        }catch (e:Exception){
            Log.d("BreakTimeAlarm", "failure : $e")
            Result.failure()
        }
    }
}