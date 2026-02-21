package com.derspusulasi.data.model

import kotlinx.serialization.Serializable

@Serializable
data class QuestionRecord(
    val id: String,
    val subject: String,
    val date: String, // Format: dd.MM.yyyy
    val total: Int,
    val correct: Int,
    val wrong: Int,
    val blank: Int,
    val net: Double // Net = Doğru - Yanlış/4.0
)
