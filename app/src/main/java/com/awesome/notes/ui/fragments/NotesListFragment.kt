package com.awesome.notes.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.awesome.notes.R
import com.awesome.notes.databinding.FragmentNotesListBinding
import com.awesome.notes.datta.entities.Note
import com.awesome.notes.ui.adapter.NotesAdapter


import com.awesome.notes.ui.viewModel.NotesViewModel


class NotesListFragment : Fragment() {

    private var _binding: FragmentNotesListBinding? = null
    private val binding get() = _binding!!

    private lateinit var notesAdapter: NotesAdapter
    private var notesList: MutableList<Note> = mutableListOf()

    private lateinit var viewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        viewModel.allNotes.observe(viewLifecycleOwner, Observer {

            notesAdapter.updateData(it)
        })
        setupRecyclerView()








        setFragmentResultListener("note_saved") { _, bundle ->
            val title = bundle.getString("noteTitle")
            val description = bundle.getString("noteDescription")
            val position = bundle.getInt("notePosition", -1)

            if (position != -1) {

                updateNoteAtPosition(Note(title ?: "", description ?: ""), position)
            } else if (title != null) {

                addNote(Note(title, description ?: ""))
            }
        }

        binding.fabAddNote.setOnClickListener {
            navToEdit(Note("", ""), -1)

        checkEmptyState()
    }
}
    private fun checkEmptyState() {

        binding.emptyStateLayout.isVisible = notesAdapter.itemCount<1
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter(notesList, requireActivity(),viewModel)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notesAdapter
        }
    }

    fun navToEdit(note: Note, position: Int) {
        val editNoteFragment = EditNoteFragment().apply {
            arguments = Bundle().apply {
                putString("noteTitle", note.title)
                putString("noteDescription", note.description)
                putInt("notePosition", position)
            }
        }

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, editNoteFragment)
            .addToBackStack(null)
            .commit()
    }


    fun addNote(note: Note) {
viewModel.insert(note)
//       notesList.add(note)
//        notesAdapter.notifyItemInserted(notesList.size - 1)
        checkEmptyState()
    }


    fun updateNoteAtPosition(updatedNote: Note, position: Int) {
        notesList[position] = updatedNote
        notesAdapter.notifyItemChanged(position)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
