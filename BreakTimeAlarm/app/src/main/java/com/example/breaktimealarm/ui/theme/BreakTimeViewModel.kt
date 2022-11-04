package com.example.breaktimealarm.ui.theme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BreakTimeViewModel:ViewModel() {
    private var _breakTime = MutableStateFlow("")
    val breakTime:StateFlow<String> = _breakTime.asStateFlow()

    fun updateBreakTime(minute:String){
        _breakTime.value = minute
    }
}