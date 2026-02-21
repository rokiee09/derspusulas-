package com.derspusulasi.data.repository

import com.derspusulasi.data.datasource.FileDataSource
import com.derspusulasi.data.model.TodoItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TodoRepository(private val fileDataSource: FileDataSource) {

    private val fileName = "todo_items.json"
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getAllTodos(): List<TodoItem> {
        return fileDataSource.readJsonList(fileName) { content ->
            json.decodeFromString<List<TodoItem>>(content)
        }
    }

    suspend fun saveTodos(todos: List<TodoItem>): Boolean {
        val jsonString = json.encodeToString(todos)
        return fileDataSource.writeJsonFile(fileName, jsonString)
    }

    suspend fun addTodo(todo: TodoItem): Boolean {
        val todos = getAllTodos().toMutableList()
        todos.add(todo)
        return saveTodos(todos)
    }

    suspend fun updateTodo(todo: TodoItem): Boolean {
        val todos = getAllTodos().toMutableList()
        val index = todos.indexOfFirst { it.id == todo.id }
        if (index != -1) {
            todos[index] = todo
            return saveTodos(todos)
        }
        return false
    }

    suspend fun deleteTodo(id: String): Boolean {
        val todos = getAllTodos().toMutableList()
        todos.removeAll { it.id == id }
        return saveTodos(todos)
    }
}
