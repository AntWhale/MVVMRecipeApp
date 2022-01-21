package com.boo.sample.mvvmrecipeapp

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(application: Application) {
    private var noteDao: NoteDao? = null
    private var allNotes: LiveData<List<Note>>? = null
    private var database: NoteDatabase? = null

    init {
        database = NoteDatabase.getInstance(application)
        noteDao = database?.noteDao()
        //??어떻게해야지 allNotes = getAllNotes()
    }

    fun insert(note: Note) {

    }

    fun update(note: Note) {

    }

    fun delete(note: Note) {

    }

    fun deleteAllNotes() {

    }

    suspend fun getAllNotes(): LiveData<List<Note>>? {
        withContext(Dispatchers.IO){
            allNotes = noteDao?.getAllNotes()
        }
        return allNotes
    }
}