package com.derspusulasi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.derspusulasi.data.model.TodoItem
import com.derspusulasi.data.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class TodoUiState(
    val todos: List<TodoItem> = emptyList(),
    val isLoading: Boolean = false
)

class TodoViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    init {
        loadTodos()
    }

    private fun loadTodos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val todos = todoRepository.getAllTodos()
            // Sort: incomplete first
            val sortedTodos = todos.sortedBy { it.isDone }
            _uiState.value = _uiState.value.copy(
                todos = sortedTodos,
                isLoading = false
            )
        }
    }

    fun addTodo(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            val todo = TodoItem(
                id = UUID.randomUUID().toString(),
                text = text.trim(),
                isDone = false,
                createdAt = System.currentTimeMillis()
            )
            todoRepository.addTodo(todo)
            loadTodos()
        }
    }

    fun toggleTodo(id: String) {
        viewModelScope.launch {
            val todo = _uiState.value.todos.find { it.id == id } ?: return@launch
            val updated = todo.copy(isDone = !todo.isDone)
            todoRepository.updateTodo(updated)
            loadTodos()
        }
    }

    fun deleteTodo(id: String) {
        viewModelScope.launch {
            todoRepository.deleteTodo(id)
            loadTodos()
        }
    }
}
