package com.survivalcoding.noteapp

import android.app.Application
import androidx.room.Room
import com.survivalcoding.noteapp.data.datasource.NoteDatabase
import com.survivalcoding.noteapp.data.repository.NoteRepositoryImpl

class App : Application() {
    val repository by lazy {
        NoteRepositoryImpl(
            Room.databaseBuilder(
                applicationContext,
                NoteDatabase::class.java,
                "TodoDatabase"
            ).build().noteDao()
        )
    }
}