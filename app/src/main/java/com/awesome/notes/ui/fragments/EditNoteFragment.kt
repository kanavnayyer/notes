package com.awesome.notes.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.awesome.notes.R
import com.awesome.notes.databinding.FragmentEditNoteBinding
import com.awesome.notes.ui.viewModel.NotesViewModel

class EditNoteFragment : Fragment() {

    private lateinit var viewModel: NotesViewModel
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private var noteId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotesViewModel::class.java)

        arguments?.let {
            noteId = it.getLong("noteId", -1L)
            binding.edittitle.setText(it.getString("noteTitle", ""))
            binding.editdescriptionNote.setText(it.getString("noteDescription", ""))
        }

        binding.fabSave.setOnClickListener {
            saveNote()
        }

        binding.fabback.setOnClickListener {
            showSaveDialog()
        }
    }

    private fun showSaveDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_save_note, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val btnYes = dialogView.findViewById<Button>(R.id.btn_yes)
        val btnNo = dialogView.findViewById<Button>(R.id.btn_no)

        btnYes.setOnClickListener {
            saveNote()
            alertDialog.dismiss()
        }

        btnNo.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            alertDialog.dismiss()
        }

        alertDialog.show()
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
            putLong("noteId", noteId ?: -1L)
        }

        parentFragmentManager.setFragmentResult("note_saved", resultBundle)
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
