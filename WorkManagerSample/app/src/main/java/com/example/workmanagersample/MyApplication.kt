package com.example.workmanagersample

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "notification channel" //表示するチャネル名
            val descriptionText = "WorkManager Sample" //チャネルの説明文
            val importance = NotificationManager.IMPORTANCE_DEFAULT //通知の重要度

            //一意のチャネルID、名前、重要度を指定したNotificationChannelオブジェクト
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            //チャネルの新規作成
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object{
        const val CHANNEL_ID = "break_time_notification"
    }
}