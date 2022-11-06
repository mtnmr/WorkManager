package com.example.breaktimealarm

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.coroutines.flow.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

const val REQUEST_TAG = "request_tag"

class BreakTimeViewModel(application: Application):ViewModel() {

    private val _uiState = MutableStateFlow(BreakTimeState())
    val uiState:StateFlow<BreakTimeState> = _uiState.asStateFlow()

    private val workManager = WorkManager.getInstance(application)

    fun updateBreakTime(minute:String){
        _uiState.update { currentState ->
            currentState.copy(
                breakTime = minute
            )
        }
    }

    private fun updateResting(b:Boolean){
        _uiState.update { currentState ->
            currentState.copy(
                isResting = b
            )
        }
    }

    private fun updateLastBreakTime(currentDate:String){
        _uiState.update { currentState ->
            currentState.copy(
               lastBreakTimeDate = currentDate
            )
        }
    }

    fun createBreakTimeNotification(currentNumber:Long, lifecycleOwner: LifecycleOwner){
        val inputData = workDataOf(
            IS_RESTING to true,
            BREAK_TIME to currentNumber,
            LAST_BREAK_TIME to SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPAN).format(Date())
        )

        val workRequest = OneTimeWorkRequestBuilder<BreakTimeNotificationWorker>()
            .setInitialDelay(currentNumber, TimeUnit.MINUTES)
            .setInputData(inputData)
            .addTag(REQUEST_TAG)
            .build()
        workManager.enqueue(workRequest)

        updateResting(true)

        workManager.getWorkInfoByIdLiveData(workRequest.id).observe(lifecycleOwner, Observer { workInfo ->
            if (workInfo != null && workInfo.state.isFinished) {
                val isRestingValue = workInfo.outputData.getBoolean(IS_RESTING, true)
                val lastBreakTimeValue = workInfo.outputData.getString(LAST_BREAK_TIME)
                updateResting(isRestingValue)
                updateLastBreakTime(lastBreakTimeValue ?: "")
            }
        })
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