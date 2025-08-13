package org.example.app

import android.content.Context

/**
 * PUBLIC_INTERFACE
 * Repository that provides a clean API for data access to the UI layer.
 * It uses a DAO backed by a local SQLite database.
 */
class NoteRepository(context: Context) {

    private val dao = NoteDao(NotesDbHelper(context.applicationContext))

    /**
     * PUBLIC_INTERFACE
     * Create a new note.
     */
    fun create(title: String, content: String): Long {
        val now = System.currentTimeMillis()
        val note = Note(
            title = title.trim(),
            content = content.trim(),
            createdAt = now,
            updatedAt = now
        )
        return dao.insert(note)
    }

    /**
     * PUBLIC_INTERFACE
     * Update an existing note.
     */
    fun update(id: Long, title: String, content: String): Int {
        val now = System.currentTimeMillis()
        val note = Note(
            id = id,
            title = title.trim(),
            content = content.trim(),
            createdAt = dao.getById(id)?.createdAt ?: now,
            updatedAt = now
        )
        return dao.update(note)
    }

    /**
     * PUBLIC_INTERFACE
     * Delete a note by id.
     */
    fun delete(id: Long): Int = dao.delete(id)

    /**
     * PUBLIC_INTERFACE
     * Fetch a note by id.
     */
    fun get(id: Long): Note? = dao.getById(id)

    /**
     * PUBLIC_INTERFACE
     * List notes with optional search query.
     */
    fun list(query: String? = null): List<Note> = dao.getAll(query)
}
