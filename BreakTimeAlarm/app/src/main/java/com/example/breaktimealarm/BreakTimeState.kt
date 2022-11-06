package com.example.breaktimealarm

data class BreakTimeState(
    val breakTime: String = "",
    val lastBreakTimeDate: String = "",
    val isResting: Boolean = false
)