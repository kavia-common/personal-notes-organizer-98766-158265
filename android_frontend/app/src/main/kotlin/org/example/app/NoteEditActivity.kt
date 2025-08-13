package org.example.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

/**
 * PUBLIC_INTERFACE
 * Screen to add a new note or edit an existing one.
 */
class NoteEditActivity : AppCompatActivity() {

    private lateinit var repository: NoteRepository
    private var noteId: Long? = null

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)
        repository = NoteRepository(this)

        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        etTitle = findViewById(R.id.etTitle)
        etContent = findViewById(R.id.etContent)

        noteId = intent.getLongExtra(NoteDetailActivity.EXTRA_NOTE_ID, -1L).let {
            if (it <= 0L) null else it
        }

        if (noteId != null) {
            title = getString(R.string.edit_note)
            repository.get(noteId!!)?.let { note ->
                etTitle.setText(note.title)
                etContent.setText(note.content)
            }
        } else {
            title = getString(R.string.new_note)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_note_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                saveNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        val titleText = etTitle.text?.toString()?.trim().orEmpty()
        val contentText = etContent.text?.toString()?.trim().orEmpty()

        if (titleText.isBlank() && contentText.isBlank()) {
            // Nothing to save; just finish.
            finish()
            return
        }

        if (noteId == null) {
            repository.create(titleText.ifBlank { getString(R.string.untitled) }, contentText)
        } else {
            repository.update(
                id = noteId!!,
                title = titleText.ifBlank { getString(R.string.untitled) },
                content = contentText
            )
        }
        finish()
    }

    companion object {
        /**
         * PUBLIC_INTERFACE
         * Start the editor for an existing note.
         */
        fun start(context: Context, noteId: Long) {
            val intent = Intent(context, NoteEditActivity::class.java)
            intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, noteId)
            context.startActivity(intent)
        }
    }
}
