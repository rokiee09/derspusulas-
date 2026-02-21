package com.derspusulasi.data.repository

import com.derspusulasi.data.datasource.FileDataSource
import com.derspusulasi.data.model.Settings
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SettingsRepository(private val fileDataSource: FileDataSource) {

    private val fileName = "settings.json"
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getSettings(): Settings {
        return fileDataSource.readJsonObject(
            fileName,
            { content -> json.decodeFromString<Settings>(content) },
            Settings() // Default settings
        )
    }

    suspend fun saveSettings(settings: Settings): Boolean {
        val jsonString = json.encodeToString(settings)
        return fileDataSource.writeJsonFile(fileName, jsonString)
    }
}
