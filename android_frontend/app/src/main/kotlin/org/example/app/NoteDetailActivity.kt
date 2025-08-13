package org.example.app

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import java.text.DateFormat
import java.util.Date

/**
 * PUBLIC_INTERFACE
 * Displays full note details and allows editing or deleting the note.
 */
class NoteDetailActivity : AppCompatActivity() {

    private lateinit var repository: NoteRepository
    private var noteId: Long = -1L
    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        repository = NoteRepository(this)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        noteId = intent.getLongExtra(EXTRA_NOTE_ID, -1L)
        if (noteId <= 0L) {
            finish()
            return
        }

        loadNote()

        val btnEdit = findViewById<Button>(R.id.btnEdit)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        btnEdit.setOnClickListener {
            NoteEditActivity.start(this, noteId)
        }

        btnDelete.setOnClickListener {
            repository.delete(noteId)
            finish()
        }
    }

    private fun loadNote() {
        note = repository.get(noteId)
        if (note == null) {
            finish()
            return
        }

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvContent = findViewById<TextView>(R.id.tvContent)
        val tvMeta = findViewById<TextView>(R.id.tvMeta)
        tvTitle.text = note!!.title
        tvContent.text = note!!.content
        val created = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            .format(Date(note!!.createdAt))
        val updated = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
            .format(Date(note!!.updatedAt))
        tvMeta.text = getString(R.string.note_meta, created, updated)
    }

    companion object {
        /**
         * PUBLIC_INTERFACE
         * Intent extra key for passing note id between activities.
         */
        const val EXTRA_NOTE_ID = "extra_note_id"
    }
}
