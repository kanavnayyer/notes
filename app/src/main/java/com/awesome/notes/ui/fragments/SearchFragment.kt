package com.awesome.notes.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.awesome.notes.databinding.FragmentSearchBinding
import com.awesome.notes.datta.entities.Note
import com.awesome.notes.ui.adapter.NotesAdapter
import com.awesome.notes.ui.viewModel.NotesViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: NotesViewModel
    private lateinit var notesAdapter: NotesAdapter
    private var allNotes: List<Note> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)


        notesAdapter = NotesAdapter(mutableListOf(), requireActivity(), viewModel)
        binding.recyclerViewSearch.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notesAdapter
        }


        viewModel.allNotes.observe(viewLifecycleOwner) { notes ->
            allNotes = notes
        }
        binding.emptyStateLayout.setOnClickListener { it.hideKeyboard() }
binding.searchfragLay.setOnClickListener { it.hideKeyboard() }
        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterNotes(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterNotes(query: String) {
        val filteredList = if (query.isNotEmpty()) {
            allNotes.filter { it.title.contains(query, ignoreCase = true) }
        } else {
            emptyList()
        }

        notesAdapter.updateData(filteredList.toMutableList())


        binding.emptyStateLayout.isVisible = filteredList.isEmpty()
    }
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
