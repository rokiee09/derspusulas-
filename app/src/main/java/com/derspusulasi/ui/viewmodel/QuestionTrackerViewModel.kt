package com.derspusulasi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.derspusulasi.data.model.QuestionRecord
import com.derspusulasi.data.repository.QuestionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class QuestionTrackerUiState(
    val records: List<QuestionRecord> = emptyList(),
    val availableDates: List<String> = emptyList(),
    val availableSubjects: List<String> = emptyList(),
    val filteredDate: String? = null, // null = show all, otherwise filter by date
    val filteredSubject: String? = null, // null = show all, otherwise filter by subject
    val searchQuery: String = "",
    val sortOrder: SortOrder = SortOrder.DATE_DESC, // Default: newest first
    val isLoading: Boolean = false,
    val selectedDate: String = "",
    val selectedSubject: String = "",
    val total: String = "",
    val correct: String = "",
    val wrong: String = "",
    val blank: String = "",
    val editingRecord: QuestionRecord? = null, // Record being edited
    val showStatistics: Boolean = false
)

enum class SortOrder {
    DATE_DESC, // Newest first
    DATE_ASC,  // Oldest first
    SUBJECT_ASC, // Subject A-Z
    SUBJECT_DESC, // Subject Z-A
    NET_DESC, // Highest net first
    NET_ASC   // Lowest net first
}

class QuestionTrackerViewModel(
    private val questionRepository: QuestionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuestionTrackerUiState())
    val uiState: StateFlow<QuestionTrackerUiState> = _uiState.asStateFlow()

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    init {
        loadRecords()
        setTodayDate()
    }

    private fun setTodayDate() {
        _uiState.value = _uiState.value.copy(
            selectedDate = dateFormat.format(Date())
        )
    }

    private fun loadRecords() {
        viewModelScope.launch {
            val currentState = _uiState.value
            _uiState.value = currentState.copy(isLoading = true)
            
            // Load all dates and subjects
            val dates = questionRepository.getAllDates()
            val subjects = questionRepository.getAllSubjects()
            
            // Load all records
            var filteredRecords = questionRepository.getAllRecords()
            
            // Apply date filter
            if (currentState.filteredDate != null) {
                filteredRecords = filteredRecords.filter { it.date == currentState.filteredDate }
            }
            
            // Apply subject filter
            if (currentState.filteredSubject != null) {
                filteredRecords = filteredRecords.filter { 
                    it.subject.equals(currentState.filteredSubject, ignoreCase = true) 
                }
            }
            
            // Apply search query
            if (currentState.searchQuery.isNotBlank()) {
                val query = currentState.searchQuery.lowercase()
                filteredRecords = filteredRecords.filter {
                    it.subject.lowercase().contains(query) ||
                    it.date.contains(query)
                }
            }
            
            // Sort records
            val sortedRecords = when (currentState.sortOrder) {
                SortOrder.DATE_DESC -> filteredRecords.sortedByDescending { 
                    try {
                        dateFormat.parse(it.date)?.time ?: 0L
                    } catch (e: Exception) {
                        0L
                    }
                }
                SortOrder.DATE_ASC -> filteredRecords.sortedBy { 
                    try {
                        dateFormat.parse(it.date)?.time ?: 0L
                    } catch (e: Exception) {
                        0L
                    }
                }
                SortOrder.SUBJECT_ASC -> filteredRecords.sortedBy { it.subject.lowercase() }
                SortOrder.SUBJECT_DESC -> filteredRecords.sortedByDescending { it.subject.lowercase() }
                SortOrder.NET_DESC -> filteredRecords.sortedByDescending { it.net }
                SortOrder.NET_ASC -> filteredRecords.sortedBy { it.net }
            }
            
            // Update state without overwriting form fields
            _uiState.value = currentState.copy(
                records = sortedRecords,
                availableDates = dates,
                availableSubjects = subjects,
                isLoading = false
            )
        }
    }
    
    fun filterByDate(date: String?) {
        _uiState.value = _uiState.value.copy(filteredDate = date)
        loadRecords()
    }
    
    fun filterBySubject(subject: String?) {
        _uiState.value = _uiState.value.copy(filteredSubject = subject)
        loadRecords()
    }
    
    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        loadRecords()
    }
    
    fun setSortOrder(order: SortOrder) {
        _uiState.value = _uiState.value.copy(sortOrder = order)
        loadRecords()
    }
    
    fun setEditingRecord(record: QuestionRecord?) {
        if (record != null) {
            _uiState.value = _uiState.value.copy(
                editingRecord = record,
                selectedSubject = record.subject,
                selectedDate = record.date,
                total = record.total.toString(),
                correct = record.correct.toString(),
                wrong = record.wrong.toString(),
                blank = record.blank.toString()
            )
        } else {
            _uiState.value = _uiState.value.copy(
                editingRecord = null,
                selectedSubject = "",
                total = "",
                correct = "",
                wrong = "",
                blank = ""
            )
            setTodayDate()
        }
    }
    
    fun updateRecord() {
        val state = _uiState.value
        val editingRecord = state.editingRecord ?: return
        
        // Validation
        if (state.selectedSubject.isBlank() || 
            state.selectedDate.isBlank() ||
            state.total.isBlank() ||
            state.correct.isBlank() ||
            state.wrong.isBlank()) {
            return
        }

        val total = state.total.toIntOrNull() ?: return
        val correct = state.correct.toIntOrNull() ?: return
        val wrong = state.wrong.toIntOrNull() ?: return
        
        if (total <= 0 || correct < 0 || wrong < 0) {
            return
        }

        val net = calculateNet() ?: return

        viewModelScope.launch {
            try {
                val blank = if (state.blank.isBlank()) {
                    total - correct - wrong
                } else {
                    state.blank.toIntOrNull() ?: (total - correct - wrong)
                }

                if (blank < 0 || correct + wrong + blank > total) {
                    return@launch
                }

                val updatedRecord = QuestionRecord(
                    id = editingRecord.id, // Keep same ID
                    subject = state.selectedSubject.trim(),
                    date = state.selectedDate,
                    total = total,
                    correct = correct,
                    wrong = wrong,
                    blank = blank,
                    net = net
                )

                val success = questionRepository.updateRecord(updatedRecord)
                if (success) {
                    loadRecords()
                    setEditingRecord(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun deleteRecord(recordId: String) {
        viewModelScope.launch {
            try {
                val success = questionRepository.deleteRecord(recordId)
                if (success) {
                    loadRecords()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun toggleStatistics() {
        _uiState.value = _uiState.value.copy(showStatistics = !_uiState.value.showStatistics)
    }
    
    fun getStatistics(): QuestionStatistics {
        val records = _uiState.value.records
        if (records.isEmpty()) {
            return QuestionStatistics(
                totalRecords = 0,
                totalNet = 0.0,
                averageNet = 0.0,
                totalQuestions = 0,
                totalCorrect = 0,
                totalWrong = 0,
                totalBlank = 0,
                subjectStatistics = emptyMap()
            )
        }
        
        val totalNet = records.sumOf { it.net }
        val averageNet = totalNet / records.size
        val totalQuestions = records.sumOf { it.total }
        val totalCorrect = records.sumOf { it.correct }
        val totalWrong = records.sumOf { it.wrong }
        val totalBlank = records.sumOf { it.blank }
        
        // Subject-based statistics
        val subjectStats = records.groupBy { it.subject }.mapValues { (_, subjectRecords) ->
            val subTotal = subjectRecords.sumOf { it.total }
            val subCorrect = subjectRecords.sumOf { it.correct }
            val subNet = subjectRecords.sumOf { it.net }
            SubjectStatistics(
                count = subjectRecords.size,
                totalQuestions = subTotal,
                totalCorrect = subCorrect,
                averageNet = subNet / subjectRecords.size
            )
        }
        
        return QuestionStatistics(
            totalRecords = records.size,
            totalNet = totalNet,
            averageNet = averageNet,
            totalQuestions = totalQuestions,
            totalCorrect = totalCorrect,
            totalWrong = totalWrong,
            totalBlank = totalBlank,
            subjectStatistics = subjectStats
        )
    }

    fun setDate(date: String) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
    }

    fun setSubject(subject: String) {
        _uiState.value = _uiState.value.copy(selectedSubject = subject)
    }

    fun setTotal(total: String) {
        _uiState.value = _uiState.value.copy(total = total)
    }

    fun setCorrect(correct: String) {
        _uiState.value = _uiState.value.copy(correct = correct)
    }

    fun setWrong(wrong: String) {
        _uiState.value = _uiState.value.copy(wrong = wrong)
    }

    fun setBlank(blank: String) {
        _uiState.value = _uiState.value.copy(blank = blank)
    }

    fun calculateNet(): Double? {
        val total = _uiState.value.total.toIntOrNull() ?: return null
        val correct = _uiState.value.correct.toIntOrNull() ?: return null
        val wrong = _uiState.value.wrong.toIntOrNull() ?: return null
        val blankStr = _uiState.value.blank

        val blank = if (blankStr.isBlank()) {
            total - correct - wrong
        } else {
            blankStr.toIntOrNull() ?: return null
        }

        // Validation
        if (total < 0 || correct < 0 || wrong < 0 || blank < 0) return null
        if (correct + wrong + blank > total) return null

        // Net = Doğru - Yanlış/4.0
        return correct - (wrong / 4.0)
    }

    fun saveRecord() {
        val state = _uiState.value
        
        // Validation
        if (state.selectedSubject.isBlank() || 
            state.selectedDate.isBlank() ||
            state.total.isBlank() ||
            state.correct.isBlank() ||
            state.wrong.isBlank()) {
            return
        }

        // Validate numbers
        val total = state.total.toIntOrNull() ?: return
        val correct = state.correct.toIntOrNull() ?: return
        val wrong = state.wrong.toIntOrNull() ?: return
        
        if (total <= 0 || correct < 0 || wrong < 0) {
            return
        }

        val net = calculateNet() ?: return

        viewModelScope.launch {
            try {
                val blank = if (state.blank.isBlank()) {
                    total - correct - wrong
                } else {
                    state.blank.toIntOrNull() ?: (total - correct - wrong)
                }

                // Final validation
                if (blank < 0 || correct + wrong + blank > total) {
                    return@launch
                }

                val record = QuestionRecord(
                    id = UUID.randomUUID().toString(),
                    subject = state.selectedSubject.trim(),
                    date = state.selectedDate,
                    total = total,
                    correct = correct,
                    wrong = wrong,
                    blank = blank,
                    net = net
                )

                val success = questionRepository.addRecord(record)
                if (success) {
                    // Reload records first
                    loadRecords()
                    
                    // Then reset form fields after a small delay to ensure UI updates
                    kotlinx.coroutines.delay(100)
                    val currentFilter = _uiState.value.filteredDate
                    _uiState.value = _uiState.value.copy(
                        selectedSubject = "",
                        total = "",
                        correct = "",
                        wrong = "",
                        blank = "",
                        filteredDate = currentFilter // Preserve filter
                    )
                    setTodayDate()
                }
            } catch (e: Exception) {
                // Error occurred, but don't crash
                e.printStackTrace()
            }
        }
    }

    // Removed deleteRecord - records should not be deleted as per requirements
}

data class QuestionStatistics(
    val totalRecords: Int,
    val totalNet: Double,
    val averageNet: Double,
    val totalQuestions: Int,
    val totalCorrect: Int,
    val totalWrong: Int,
    val totalBlank: Int,
    val subjectStatistics: Map<String, SubjectStatistics> = emptyMap()
)

data class SubjectStatistics(
    val count: Int,
    val totalQuestions: Int,
    val totalCorrect: Int,
    val averageNet: Double
)
