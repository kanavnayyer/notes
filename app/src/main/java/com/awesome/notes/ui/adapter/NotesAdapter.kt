package com.awesome.notes.ui.adapter


import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.awesome.notes.datta.entities.Note
import com.awesome.notes.R
import com.awesome.notes.ui.fragments.EditNoteFragment
import com.awesome.notes.databinding.ItemNoteBinding
import com.awesome.notes.ui.viewModel.NotesViewModel


import kotlin.random.Random

class NotesAdapter(
    private val notes: MutableList<Note>,
    private val fragmentActivity: FragmentActivity,

    private val viewModel: NotesViewModel
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note, position)
    }

    fun updateData(newData: List<Note>){
        notes.clear()
        notes.addAll(newData)
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(private val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {

        private var isDeleteMode = false

        init {
            binding.root.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {

                    isDeleteMode = !isDeleteMode
                    val note = notes[position]

                    val drawable = ContextCompat.getDrawable(fragmentActivity, R.drawable.baseline_delete_24)
                    drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

                    binding.root.setBackgroundColor(
                        if (isDeleteMode) Color.RED else getRandomColor()
                    )

                    if (isDeleteMode) {
                        val spannableString = SpannableString(" ")
                        val drawableSpan = ImageSpan(drawable!!, ImageSpan.ALIGN_CENTER)
                        spannableString.setSpan(drawableSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                        binding.tvNoteTitle.text = spannableString
                        binding.tvNoteTitle.gravity = Gravity.CENTER

                        binding.tvNoteTitle.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                        binding.tvNoteTitle.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT

                        Toast.makeText(fragmentActivity, "Long press again to delete", Toast.LENGTH_SHORT).show()
                    } else {
                        binding.tvNoteTitle.text = note.title
                        binding.tvNoteTitle.gravity = Gravity.START
                        binding.tvNoteTitle.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        binding.tvNoteTitle.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT

                        Toast.makeText(fragmentActivity, "Delete mode deactivated", Toast.LENGTH_SHORT).show()
                    }

                    return@setOnLongClickListener true
                }
                false
            }

            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (isDeleteMode) {

                        val note = notes[position]
                        deleteNoteFromRoomDb(note)
                        notes.removeAt(position)
                        notifyItemRemoved(position)
                        Toast.makeText(fragmentActivity, "Note deleted", Toast.LENGTH_SHORT).show()
                        isDeleteMode = false
                        binding.root.setBackgroundColor(getRandomColor())
                    } else {

                        val note = notes[position]
                        val editNoteFragment = EditNoteFragment().apply {
                            arguments = Bundle().apply {
                                putString("noteTitle", note.title)
                                putString("noteDescription", note.description)
                                putInt("notePosition", position)
                            }
                        }
                        fragmentActivity.supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, editNoteFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }
        }

        fun bind(note: Note, position: Int) {
            binding.tvNoteTitle.text = note.title
            binding.tvNoteContent.text = note.description
            binding.root.setBackgroundColor(getRandomColor())
        }

        private fun deleteNoteFromRoomDb(note: Note) {

           viewModel.deleteNote(note)
        }
    }


    private fun getRandomColor(): Int {
        val colors = arrayOf(Color.LTGRAY, Color.MAGENTA, Color.BLUE, Color.GREEN)
        return colors[Random.nextInt(colors.size)]
    }
}
