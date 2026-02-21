package com.derspusulasi.data.repository

import com.derspusulasi.data.datasource.FileDataSource
import com.derspusulasi.data.model.QuestionRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class QuestionRepository(private val fileDataSource: FileDataSource) {

    private val recordsDir = "question_records"
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Get file name for a specific date (format: dd.MM.yyyy -> dd_MM_yyyy.json)
     */
    private fun getFileNameForDate(date: String): String {
        // Convert dd.MM.yyyy to dd_MM_yyyy.json
        return "${date.replace(".", "_")}.json"
    }

    /**
     * Get all records from all date files
     */
    suspend fun getAllRecords(): List<QuestionRecord> {
        val allRecords = mutableListOf<QuestionRecord>()
        val recordsDirFile = File(fileDataSource.getFilesDir(), recordsDir)
        
        if (recordsDirFile.exists() && recordsDirFile.isDirectory) {
            recordsDirFile.listFiles()?.forEach { file ->
                if (file.name.endsWith(".json")) {
                    try {
                        val content = file.readText()
                        if (content.isNotBlank()) {
                            val records = json.decodeFromString<List<QuestionRecord>>(content)
                            allRecords.addAll(records)
                        }
                    } catch (e: Exception) {
                        // Skip corrupted files
                    }
                }
            }
        }
        
        return allRecords
    }

    /**
     * Get records for a specific date
     */
    suspend fun getRecordsByDate(date: String): List<QuestionRecord> {
        val fileName = getFileNameForDate(date)
        val file = File(File(fileDataSource.getFilesDir(), recordsDir), fileName)
        
        return if (file.exists() && file.length() > 0) {
            try {
                val content = file.readText()
                json.decodeFromString<List<QuestionRecord>>(content)
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    /**
     * Get all available dates that have records
     */
    suspend fun getAllDates(): List<String> {
        val dates = mutableSetOf<String>()
        val recordsDirFile = File(fileDataSource.getFilesDir(), recordsDir)
        
        if (recordsDirFile.exists() && recordsDirFile.isDirectory) {
            recordsDirFile.listFiles()?.forEach { file ->
                if (file.name.endsWith(".json")) {
                    // Convert dd_MM_yyyy.json back to dd.MM.yyyy
                    val dateStr = file.name.replace(".json", "").replace("_", ".")
                    dates.add(dateStr)
                }
            }
        }
        
        return dates.sortedDescending()
    }

    /**
     * Save records for a specific date
     */
    private suspend fun saveRecordsForDate(date: String, records: List<QuestionRecord>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val fileName = getFileNameForDate(date)
                val recordsDirFile = File(fileDataSource.getFilesDir(), recordsDir)
                
                // Ensure directory exists
                if (!recordsDirFile.exists()) {
                    recordsDirFile.mkdirs()
                }
                
                val file = File(recordsDirFile, fileName)
                val tempFile = File(recordsDirFile, "$fileName.tmp")
                
                val jsonString = json.encodeToString(records)
                
                // Write to temp file first (atomic write)
                tempFile.writeText(jsonString)
                
                // Atomic rename
                val renamed = tempFile.renameTo(file)
                if (!renamed) {
                    // If rename failed, try direct write
                    file.writeText(jsonString)
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    /**
     * Add a new record (saves to date-specific file)
     */
    suspend fun addRecord(record: QuestionRecord): Boolean {
        val existingRecords = getRecordsByDate(record.date).toMutableList()
        existingRecords.add(record)
        return saveRecordsForDate(record.date, existingRecords)
    }
    
    /**
     * Update an existing record
     */
    suspend fun updateRecord(updatedRecord: QuestionRecord): Boolean {
        val allRecords = getAllRecords().toMutableList()
        val index = allRecords.indexOfFirst { it.id == updatedRecord.id }
        
        if (index == -1) return false
        
        // Remove old record
        val oldRecord = allRecords[index]
        val oldDateRecords = getRecordsByDate(oldRecord.date).toMutableList()
        oldDateRecords.removeAll { it.id == oldRecord.id }
        saveRecordsForDate(oldRecord.date, oldDateRecords)
        
        // Add updated record to its date file
        val newDateRecords = getRecordsByDate(updatedRecord.date).toMutableList()
        newDateRecords.add(updatedRecord)
        return saveRecordsForDate(updatedRecord.date, newDateRecords)
    }
    
    /**
     * Delete a record
     */
    suspend fun deleteRecord(recordId: String): Boolean {
        val allRecords = getAllRecords()
        val record = allRecords.find { it.id == recordId } ?: return false
        
        val dateRecords = getRecordsByDate(record.date).toMutableList()
        dateRecords.removeAll { it.id == recordId }
        return saveRecordsForDate(record.date, dateRecords)
    }
    
    /**
     * Get all unique subjects
     */
    suspend fun getAllSubjects(): List<String> {
        val subjects = getAllRecords().map { it.subject }.distinct()
        return subjects.sorted()
    }
    
    /**
     * Get records by subject
     */
    suspend fun getRecordsBySubject(subject: String): List<QuestionRecord> {
        return getAllRecords().filter { it.subject.equals(subject, ignoreCase = true) }
    }
}
