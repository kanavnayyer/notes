package com.awesome.notes.datta.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_app")
data class Note(  @ColumnInfo(name = "title") val title: String,
                  @ColumnInfo(name="description") val description: String,
  @PrimaryKey(autoGenerate = true) val id: Long = 0)


