package org.example.app

/**
 * PUBLIC_INTERFACE
 * Data model representing a note entity stored in local SQLite database.
 *
 * @property id Unique identifier of the note. Null for notes not yet persisted.
 * @property title Short title of the note.
 * @property content Main text content of the note.
 * @property createdAt Unix epoch millis when the note was created.
 * @property updatedAt Unix epoch millis when the note was last updated.
 */
data class Note(
    val id: Long? = null,
    val title: String,
    val content: String,
    val createdAt: Long,
    val updatedAt: Long
)
