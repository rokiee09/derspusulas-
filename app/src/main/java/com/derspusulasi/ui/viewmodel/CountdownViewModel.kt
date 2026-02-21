package com.derspusulasi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CountdownUiState(
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val remainingSeconds: Long = 0,
    val totalSeconds: Long = 0
)

class CountdownViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CountdownUiState())
    val uiState: StateFlow<CountdownUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    fun setTime(minutes: Int, seconds: Int) {
        val total = (minutes * 60 + seconds).toLong()
        if (!_uiState.value.isRunning) {
            _uiState.value = _uiState.value.copy(
                remainingSeconds = total,
                totalSeconds = total
            )
        }
    }

    fun startTimer() {
        if (_uiState.value.isRunning || _uiState.value.remainingSeconds <= 0) return

        _uiState.value = _uiState.value.copy(
            isRunning = true,
            isPaused = false
        )

        timerJob = viewModelScope.launch {
            while (_uiState.value.remainingSeconds > 0 && _uiState.value.isRunning) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    remainingSeconds = _uiState.value.remainingSeconds - 1
                )
            }

            if (_uiState.value.remainingSeconds == 0L) {
                onTimerFinished()
            }
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(
            isRunning = false,
            isPaused = true
        )
    }

    fun resetTimer() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(
            isRunning = false,
            isPaused = false,
            remainingSeconds = _uiState.value.totalSeconds
        )
    }

    private fun onTimerFinished() {
        _uiState.value = _uiState.value.copy(
            isRunning = false,
            isPaused = false
        )
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
