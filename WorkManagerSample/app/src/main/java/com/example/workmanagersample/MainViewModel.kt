package com.example.workmanagersample

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

const val MINUTE = "minute"
const val REQUEST_TAG = "notification reqyest"

class MainViewModel(application: Application) :ViewModel() {

    private val workManager = WorkManager.getInstance(application)

    fun createBreakTimeNotification(minute:Long){
        val inputData = workDataOf(
            MINUTE to minute,
        )

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(minute, TimeUnit.SECONDS)
            .setInputData(inputData)
            .addTag(REQUEST_TAG)
            .build()
        workManager.enqueue(workRequest)
    }

    class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}