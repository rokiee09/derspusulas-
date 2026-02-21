package com.derspusulasi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.derspusulasi.data.repository.SettingsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PomodoroUiState(
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val isFocusMode: Boolean = true, // true = Focus, false = Break
    val remainingSeconds: Long = 0,
    val totalSeconds: Long = 0,
    val focusMinutes: Int = 25,
    val breakMinutes: Int = 5
)

class PomodoroViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PomodoroUiState())
    val uiState: StateFlow<PomodoroUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            _uiState.value = _uiState.value.copy(
                focusMinutes = settings.pomodoroFocusMinutes,
                breakMinutes = settings.pomodoroBreakMinutes,
                remainingSeconds = (settings.pomodoroFocusMinutes * 60).toLong(),
                totalSeconds = (settings.pomodoroFocusMinutes * 60).toLong()
            )
        }
    }

    fun startTimer() {
        if (_uiState.value.isRunning) return

        val currentState = _uiState.value
        val startSeconds = if (currentState.remainingSeconds > 0) {
            currentState.remainingSeconds
        } else {
            val minutes = if (currentState.isFocusMode) currentState.focusMinutes else currentState.breakMinutes
            (minutes * 60).toLong()
        }

        _uiState.value = currentState.copy(
            isRunning = true,
            isPaused = false,
            remainingSeconds = startSeconds,
            totalSeconds = startSeconds
        )

        timerJob = viewModelScope.launch {
            while (_uiState.value.remainingSeconds > 0 && _uiState.value.isRunning) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    remainingSeconds = _uiState.value.remainingSeconds - 1
                )
            }

            // Timer finished
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
        val currentState = _uiState.value
        val minutes = if (currentState.isFocusMode) currentState.focusMinutes else currentState.breakMinutes
        _uiState.value = currentState.copy(
            isRunning = false,
            isPaused = false,
            remainingSeconds = (minutes * 60).toLong(),
            totalSeconds = (minutes * 60).toLong()
        )
    }

    private fun onTimerFinished() {
        val currentState = _uiState.value
        // Timer finished - notification will be sent from UI layer
        // Switch mode for next session
        val newMode = !currentState.isFocusMode
        val minutes = if (newMode) currentState.focusMinutes else currentState.breakMinutes

        _uiState.value = currentState.copy(
            isRunning = false,
            isPaused = false,
            isFocusMode = newMode,
            remainingSeconds = (minutes * 60).toLong(),
            totalSeconds = (minutes * 60).toLong()
        )
    }

    fun updateFocusMinutes(minutes: Int) {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            val updatedSettings = settings.copy(pomodoroFocusMinutes = minutes)
            settingsRepository.saveSettings(updatedSettings)
            _uiState.value = _uiState.value.copy(focusMinutes = minutes)
            if (!_uiState.value.isRunning && _uiState.value.isFocusMode) {
                resetTimer()
            }
        }
    }

    fun updateBreakMinutes(minutes: Int) {
        viewModelScope.launch {
            val settings = settingsRepository.getSettings()
            val updatedSettings = settings.copy(pomodoroBreakMinutes = minutes)
            settingsRepository.saveSettings(updatedSettings)
            _uiState.value = _uiState.value.copy(breakMinutes = minutes)
            if (!_uiState.value.isRunning && !_uiState.value.isFocusMode) {
                resetTimer()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
