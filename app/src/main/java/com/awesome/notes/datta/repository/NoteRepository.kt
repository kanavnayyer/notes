package com.awesome.notes.datta.repository


import android.util.Log
import androidx.lifecycle.LiveData
import com.awesome.notes.datta.dao.NoteDao
import com.awesome.notes.datta.entities.Note



class NoteRepository(private val noteDao: NoteDao) {


    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()


    suspend fun insert(note: Note) {
        Log.d("NoteRepository", "Inserting note: ${note.title}")
        noteDao.insert(note)
        Log.d("NoteRepository", "Note inserted: ${note.title}")
    }


    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }
    suspend fun update(note: Note) {
        noteDao.update(note)
    }

}

