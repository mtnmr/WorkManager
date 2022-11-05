package com.example.breaktimealarm

import android.app.Application
import androidx.lifecycle.*
import androidx.work.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit

const val REQUEST_TAG = "request_tag"

class BreakTimeViewModel(private val application: Application):ViewModel() {
    private val _breakTime = MutableStateFlow("")
    val breakTime:StateFlow<String> = _breakTime.asStateFlow()

    private var _isResting = MutableStateFlow(false)
    val isResting:StateFlow<Boolean> = _isResting.asStateFlow()

    private val workManager = WorkManager.getInstance(application)


    fun updateBreakTime(minute:String){
        _breakTime.value = minute
    }

    private fun updateResting(b:Boolean){
        _isResting.value = b
    }

    fun createBreakTimeNotification(currentNumber:Long, lifecycleOwner: LifecycleOwner){
        val workRequest = OneTimeWorkRequestBuilder<BreakTimeNotificationWorker>()
            .setInitialDelay(currentNumber, TimeUnit.SECONDS)
            .setInputData(createIsRestingInputData())
            .setInputData(createBreakTimeInputData(currentNumber))
            .addTag(REQUEST_TAG)
            .build()
        workManager.enqueue(workRequest)

        updateResting(true)

        workManager.getWorkInfoByIdLiveData(workRequest.id).observe(lifecycleOwner, Observer { workInfo ->
            if (workInfo != null && workInfo.state.isFinished) {
                val value = workInfo.outputData.getBoolean(FINISH, true)
                updateResting(value)
            }
        })
    }

    private fun createIsRestingInputData(): Data {
        val builder = Data.Builder()
        return builder.putBoolean(FINISH, true).build()
    }

    private fun createBreakTimeInputData(number:Long): Data{
        val builder = Data.Builder()
        return builder.putLong(BREAK_TIME, number).build()
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