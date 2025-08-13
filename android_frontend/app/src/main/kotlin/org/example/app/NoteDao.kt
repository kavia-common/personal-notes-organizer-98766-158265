package org.example.app

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

/**
 * PUBLIC_INTERFACE
 * Data Access Object that encapsulates SQL operations for notes.
 */
class NoteDao(private val dbHelper: NotesDbHelper) {

    /**
     * PUBLIC_INTERFACE
     * Insert a new note.
     *
     * @param note Note model without an id.
     * @return the newly inserted row id.
     */
    fun insert(note: Note): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(NotesDbHelper.COL_TITLE, note.title)
            put(NotesDbHelper.COL_CONTENT, note.content)
            put(NotesDbHelper.COL_CREATED_AT, note.createdAt)
            put(NotesDbHelper.COL_UPDATED_AT, note.updatedAt)
        }
        return db.insert(NotesDbHelper.TABLE_NOTES, null, values)
    }

    /**
     * PUBLIC_INTERFACE
     * Update an existing note by id.
     *
     * @param note Note model with a non-null id to update.
     * @return number of rows affected.
     */
    fun update(note: Note): Int {
        requireNotNull(note.id) { "Note id is required for update" }
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(NotesDbHelper.COL_TITLE, note.title)
            put(NotesDbHelper.COL_CONTENT, note.content)
            put(NotesDbHelper.COL_UPDATED_AT, note.updatedAt)
        }
        return db.update(
            NotesDbHelper.TABLE_NOTES,
            values,
            "${NotesDbHelper.COL_ID}=?",
            arrayOf(note.id.toString())
        )
    }

    /**
     * PUBLIC_INTERFACE
     * Delete a note by id.
     *
     * @param id Note id.
     * @return number of rows deleted.
     */
    fun delete(id: Long): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            NotesDbHelper.TABLE_NOTES,
            "${NotesDbHelper.COL_ID}=?",
            arrayOf(id.toString())
        )
    }

    /**
     * PUBLIC_INTERFACE
     * Get a single note by id.
     *
     * @param id Note id.
     * @return Note if found, or null.
     */
    fun getById(id: Long): Note? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            NotesDbHelper.TABLE_NOTES,
            ALL_COLUMNS,
            "${NotesDbHelper.COL_ID}=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        cursor.use {
            return if (it.moveToFirst()) cursorToNote(it) else null
        }
    }

    /**
     * PUBLIC_INTERFACE
     * Get all notes, optionally filtering by a search query against title and content.
     * Results are ordered by updatedAt descending.
     *
     * @param query Optional search string.
     * @return list of notes.
     */
    fun getAll(query: String? = null): List<Note> {
        val db = dbHelper.readableDatabase
        val (selection, args) = if (!query.isNullOrBlank()) {
            Pair(
                "(${NotesDbHelper.COL_TITLE} LIKE ? OR ${NotesDbHelper.COL_CONTENT} LIKE ?)",
                arrayOf("%$query%", "%$query%")
            )
        } else {
            Pair(null, null)
        }
        val cursor = db.query(
            NotesDbHelper.TABLE_NOTES,
            ALL_COLUMNS,
            selection,
            args,
            null,
            null,
            "${NotesDbHelper.COL_UPDATED_AT} DESC"
        )
        val notes = mutableListOf<Note>()
        cursor.use {
            while (it.moveToNext()) {
                notes.add(cursorToNote(it))
            }
        }
        return notes
    }

    private fun cursorToNote(c: Cursor): Note {
        val id = c.getLong(c.getColumnIndexOrThrow(NotesDbHelper.COL_ID))
        val title = c.getString(c.getColumnIndexOrThrow(NotesDbHelper.COL_TITLE))
        val content = c.getString(c.getColumnIndexOrThrow(NotesDbHelper.COL_CONTENT))
        val createdAt = c.getLong(c.getColumnIndexOrThrow(NotesDbHelper.COL_CREATED_AT))
        val updatedAt = c.getLong(c.getColumnIndexOrThrow(NotesDbHelper.COL_UPDATED_AT))
        return Note(
            id = id,
            title = title,
            content = content,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }

    companion object {
        private val ALL_COLUMNS = arrayOf(
            NotesDbHelper.COL_ID,
            NotesDbHelper.COL_TITLE,
            NotesDbHelper.COL_CONTENT,
            NotesDbHelper.COL_CREATED_AT,
            NotesDbHelper.COL_UPDATED_AT
        )
    }
}
