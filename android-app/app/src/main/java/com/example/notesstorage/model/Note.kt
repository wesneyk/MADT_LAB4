package com.example.notesstorage.model

import java.util.UUID

enum class StorageType {
    SQLITE,
    JSON_FILE
}

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
