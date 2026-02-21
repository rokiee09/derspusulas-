package com.derspusulasi.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val pomodoroFocusMinutes: Int = 25,
    val pomodoroBreakMinutes: Int = 5,
    val screenBrightness: Float = -1f // -1 = sistem ayarı, 0.0-1.0 = özel parlaklık
)
