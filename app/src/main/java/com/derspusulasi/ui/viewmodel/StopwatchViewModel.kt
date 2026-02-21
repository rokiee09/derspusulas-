package com.derspusulasi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Lap(
    val id: String,
    val time: Long, // milliseconds
    val lapNumber: Int
)

data class StopwatchUiState(
    val isRunning: Boolean = false,
    val elapsedTime: Long = 0, // milliseconds
    val laps: List<Lap> = emptyList()
)

class StopwatchViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(StopwatchUiState())
    val uiState: StateFlow<StopwatchUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null
    private var startTime: Long = 0
    private var pausedTime: Long = 0

    fun startStopwatch() {
        if (_uiState.value.isRunning) return

        val currentElapsed = _uiState.value.elapsedTime
        startTime = System.currentTimeMillis() - currentElapsed

        _uiState.value = _uiState.value.copy(isRunning = true)

        timerJob = viewModelScope.launch {
            while (_uiState.value.isRunning) {
                delay(10) // Update every 10ms for smooth display
                val current = System.currentTimeMillis() - startTime
                _uiState.value = _uiState.value.copy(elapsedTime = current)
            }
        }
    }

    fun pauseStopwatch() {
        timerJob?.cancel()
        pausedTime = _uiState.value.elapsedTime
        _uiState.value = _uiState.value.copy(isRunning = false)
    }

    fun resetStopwatch() {
        timerJob?.cancel()
        startTime = 0
        pausedTime = 0
        _uiState.value = StopwatchUiState()
    }

    fun addLap() {
        if (!_uiState.value.isRunning) return

        val currentElapsed = _uiState.value.elapsedTime
        val lapNumber = _uiState.value.laps.size + 1
        val lap = Lap(
            id = java.util.UUID.randomUUID().toString(),
            time = currentElapsed,
            lapNumber = lapNumber
        )

        _uiState.value = _uiState.value.copy(
            laps = _uiState.value.laps + lap
        )
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
