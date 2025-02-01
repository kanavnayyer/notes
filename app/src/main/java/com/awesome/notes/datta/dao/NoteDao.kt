package com.awesome.notes.datta.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.awesome.notes.datta.entities.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM notes_app ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>
}
