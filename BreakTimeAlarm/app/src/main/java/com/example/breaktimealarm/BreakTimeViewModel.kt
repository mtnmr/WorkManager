package com.example.breaktimealarm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit

class BreakTimeViewModel(val application: Application):ViewModel() {
    private var _breakTime = MutableStateFlow("")
    val breakTime:StateFlow<String> = _breakTime.asStateFlow()

    fun updateBreakTime(minute:String){
        _breakTime.value = minute
    }

    fun createBreakTimeNotification(currentNumber:String){
        val currentBreakTime = currentNumber.toIntOrNull()

        val workRequest = OneTimeWorkRequestBuilder<BreakTimeNotificationWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(application)
            .enqueue(workRequest)
    }
}

class BreakTimeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(BreakTimeViewModel::class.java)) {
            BreakTimeViewModel(application) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}