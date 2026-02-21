package com.derspusulasi.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TodoItem(
    val id: String,
    val text: String,
    val isDone: Boolean,
    val createdAt: Long
)
