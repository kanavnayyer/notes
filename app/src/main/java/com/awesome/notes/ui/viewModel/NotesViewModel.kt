package com.awesome.notes.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.awesome.notes.datta.repository.NoteRepository
import com.awesome.notes.datta.db.NotesDatabase
import com.awesome.notes.datta.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {


    private val noteRepository: NoteRepository
    val allNotes: LiveData<List<Note>>


    init {

        val dao = NotesDatabase.getDatabase(application).noteDao()
        noteRepository = NoteRepository(dao)
        allNotes = noteRepository.allNotes
    }

    fun insert(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.insert(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.delete(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            noteRepository.update(note)
        }
    }
}
