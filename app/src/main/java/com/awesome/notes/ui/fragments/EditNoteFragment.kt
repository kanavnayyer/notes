package com.awesome.notes.ui.fragments




import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import com.awesome.notes.databinding.FragmentEditNoteBinding



class EditNoteFragment : Fragment() {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private var notePosition: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val title = it.getString("noteTitle") ?: ""
            val description = it.getString("noteDescription") ?: ""
            notePosition = it.getInt("notePosition", -1)


            binding.edittitle.setText(title)
            binding.editdescriptionNote.setText(description)
        }

        binding.fabSave.setOnClickListener {
            saveNote()
        }


        binding.fabback.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun saveNote() {
        val title = binding.edittitle.text.toString().trim()
        val description = binding.editdescriptionNote.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Title cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }


        val resultBundle = Bundle().apply {
            putString("noteTitle", title)
            putString("noteDescription", description)
            putInt("notePosition", notePosition ?: -1)
        }

        parentFragmentManager.setFragmentResult("note_saved", resultBundle)


        requireActivity().supportFragmentManager.popBackStack()
    }
}

