package com.survivalcoding.noteapp.domain.usecase

import com.survivalcoding.noteapp.domain.repository.NoteRepository

class SortByTimestampAscUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke() = repository.sortByTimestampAsc()
}