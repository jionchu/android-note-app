package com.survivalcoding.noteapp.data.datasource

import com.survivalcoding.noteapp.domain.model.Note

interface NoteDataSource {
    suspend fun getNoteById(id: Int): Note
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun sortByColorAsc(): List<Note>
    suspend fun sortByColorDesc(): List<Note>
    suspend fun sortByTitleAsc(): List<Note>
    suspend fun sortByTitleDesc(): List<Note>
    suspend fun sortByTimestampAsc(): List<Note>
    suspend fun sortByTimestampDesc(): List<Note>
}