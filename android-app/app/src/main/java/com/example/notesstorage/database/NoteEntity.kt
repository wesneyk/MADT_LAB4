package com.example.notesstorage.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.notesstorage.model.Note

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long
) {
    fun toDomain(): Note = Note(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun fromDomain(note: Note): NoteEntity = NoteEntity(
            id = note.id,
            title = note.title,
            content = note.content,
            createdAt = note.createdAt,
            updatedAt = note.updatedAt
        )
    }
}
