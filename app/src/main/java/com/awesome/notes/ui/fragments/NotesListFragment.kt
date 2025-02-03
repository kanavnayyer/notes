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
    private lateinit var viewModel: NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        setupRecyclerView()
        observeNotes()
        checkEmptyState()

        setFragmentResultListener("note_saved") { _, bundle ->
            val title = bundle.getString("noteTitle")
            val description = bundle.getString("noteDescription")
            val noteId = bundle.getLong("noteId", -1L)

            if (noteId != -1L) {

                viewModel.updateNote(Note(title ?: "", description ?: "", noteId))
            } else if (title != null) {

                viewModel.insert(Note(title, description ?: ""))
            }
        }

        binding.fabAddNote.setOnClickListener {
            openEditFragment(null)
        }

        binding.fabSearch.setOnClickListener {
            openSearchFragment()
        }



    }

    private fun observeNotes() {
        viewModel.allNotes.observe(viewLifecycleOwner, Observer { notes ->
            notesAdapter.updateData(notes)
            checkEmptyState()
        })
    }

    private fun checkEmptyState() {
        binding.emptyStateLayout.isVisible = notesAdapter.itemCount == 0
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter(mutableListOf(), requireActivity(), viewModel)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notesAdapter

        }
    }

    private fun openEditFragment(note: Note?) {
        val editNoteFragment = EditNoteFragment().apply {
            arguments = Bundle().apply {
                putLong("noteId", note?.id ?: -1L)
                putString("noteTitle", note?.title ?: "")
                putString("noteDescription", note?.description ?: "")
            }
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, editNoteFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun openSearchFragment() {
        val searchFragment = SearchFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, searchFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
