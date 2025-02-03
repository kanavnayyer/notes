package com.awesome.notes.ui.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.awesome.notes.R
import com.awesome.notes.databinding.FragmentEditNoteBinding
import com.awesome.notes.ui.viewModel.NotesViewModel

class EditNoteFragment : Fragment() {

    private var initialTitle=""
    private var initialDescription=""
    private lateinit var viewModel: NotesViewModel
    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!

    private var noteId: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
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
       initialTitle=it.getString("noteTitle", "")
          initialDescription=it.getString("noteDescription", "")
        }

        binding.fabSave.setOnClickListener {

            if (hasChanges()) {
                showSaveDialog(1)

            } else {
                requireActivity().supportFragmentManager.popBackStack()
            }


        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

            handleCustomBackPress()}

binding.layeditnote.setOnClickListener {
    it.hideKeyboard()
}
        binding.fabback.setOnClickListener {

            if (hasChanges()) {
                showSaveDialog(0)
            } else {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }

    }

    @SuppressLint("ResourceAsColor")
    private fun handleCustomBackPress() {
        if (hasChanges()) {

            showSaveDialog(0)

        } else {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun hasChanges(): Boolean {
        val currentTitle = binding.edittitle.text.toString().trim()
        val currentDescription = binding.editdescriptionNote.text.toString().trim()
        return currentTitle != initialTitle || currentDescription != initialDescription
    }


    private fun showSaveDialog(i: Int) {


        val dialogView = layoutInflater.inflate(R.layout.dialog_save_note, null)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)

            .setCancelable(false)
            .create()
        var backgroundColor = ContextCompat.getColor(requireContext(), R.color.blak)
val txt=dialogView.findViewById<TextView>(R.id.btntxt)
        val btnYes = dialogView.findViewById<Button>(R.id.btn_yes)
        val btnNo = dialogView.findViewById<Button>(R.id.btn_no)
        if(i==0){
            txt.setText(R.string.savebtn)
            btnNo.setText(R.string.discard)
        }else{
            txt.setText(R.string.save_changes)
            btnNo.setText(R.string.no)

        }
        binding.layeditnote.setBackgroundColor(backgroundColor)



        btnYes.setOnClickListener {
            saveNote()
            backgroundColor = ContextCompat.getColor(requireContext(), R.color.black)
            binding.layeditnote.setBackgroundColor(backgroundColor)
            alertDialog.dismiss()
        }

        btnNo.setOnClickListener {
            backgroundColor = ContextCompat.getColor(requireContext(), R.color.black)
            binding.layeditnote.setBackgroundColor(backgroundColor)
            if (i==0){
            requireActivity().supportFragmentManager.popBackStack()
            alertDialog.dismiss()}
            else{
                alertDialog.dismiss()
            }
        }

        alertDialog.show()
    }

    private fun saveNote() {
        val title = binding.edittitle.text.toString().trim()
        val description = binding.editdescriptionNote.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(requireContext(),
                getString(R.string.title_cannot_be_empty), Toast.LENGTH_SHORT).show()
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
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
