package org.example.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import java.text.DateFormat
import java.util.Date

/**
 * PUBLIC_INTERFACE
 * RecyclerView Adapter to render the list of notes.
 */
class NoteListAdapter(
    private val onItemClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>() {

    private val items = mutableListOf<Note>()

    /**
     * PUBLIC_INTERFACE
     * Provide a new list of notes to display.
     */
    fun submitList(newItems: List<Note>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class NoteViewHolder(
        itemView: View,
        private val onItemClick: (Note) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val card = itemView.findViewById<MaterialCardView>(R.id.cardRoot)
        private val title = itemView.findViewById<TextView>(R.id.tvTitle)
        private val content = itemView.findViewById<TextView>(R.id.tvContent)
        private val updated = itemView.findViewById<TextView>(R.id.tvUpdated)

        fun bind(note: Note) {
            title.text = note.title
            content.text = snippet(note.content)
            updated.text = DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM, DateFormat.SHORT
            ).format(Date(note.updatedAt))
            card.setOnClickListener { onItemClick(note) }
        }

        private fun snippet(text: String, maxLen: Int = 120): String {
            val oneLine = text.replace(Regex("\\s+"), " ").trim()
            return if (oneLine.length <= maxLen) oneLine else oneLine.substring(0, maxLen) + "…"
        }
    }
}
