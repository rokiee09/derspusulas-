package com.derspusulasi.data.datasource

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class FileDataSource(private val context: Context) {

    private val filesDir: File = context.filesDir

    /**
     * Reads JSON file from internal storage
     * Returns null if file doesn't exist or is empty
     */
    suspend fun readJsonFile(fileName: String): String? = withContext(Dispatchers.IO) {
        try {
            val file = File(filesDir, fileName)
            if (file.exists() && file.length() > 0) {
                file.readText()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Writes JSON content to file atomically
     * Uses temp file + rename to prevent data corruption
     */
    suspend fun writeJsonFile(fileName: String, content: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val file = File(filesDir, fileName)
            val tempFile = File(filesDir, "$fileName.tmp")
            
            // Write to temp file first
            tempFile.writeText(content)
            
            // Atomic rename
            tempFile.renameTo(file)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Safe parse: returns default empty list if file doesn't exist or parse fails
     */
    suspend fun <T> readJsonList(
        fileName: String,
        deserializer: (String) -> List<T>
    ): List<T> {
        val content = readJsonFile(fileName)
        return if (content != null) {
            try {
                deserializer(content)
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    /**
     * Safe parse: returns default value if file doesn't exist or parse fails
     */
    suspend fun <T> readJsonObject(
        fileName: String,
        deserializer: (String) -> T,
        defaultValue: T
    ): T {
        val content = readJsonFile(fileName)
        return if (content != null) {
            try {
                deserializer(content)
            } catch (e: Exception) {
                defaultValue
            }
        } else {
            defaultValue
        }
    }

    /**
     * Get files directory for direct file operations
     */
    fun getFilesDir(): File = filesDir
}
