package com.boo.sample.mvvmrecipeapp

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(application: Application) {
    private var noteDao: NoteDao? = null
    private var allNotes: LiveData<List<Note>>? = null
    private var database: NoteDatabase? = null
    private var scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    init {
        database = NoteDatabase.getInstance(application, scope)
        noteDao = database?.noteDao()
        allNotes = getAllNotes()

    }

    suspend fun insert(note: Note) {
        withContext(Dispatchers.IO){
            noteDao?.insert(note)
        }
    }

    suspend fun update(note: Note) {
        withContext(Dispatchers.IO){
            noteDao?.update(note)
        }
    }

    suspend fun delete(note: Note) {
        withContext(Dispatchers.IO){
            noteDao?.delete(note)
        }
    }

    suspend fun deleteAllNotes() {
        withContext(Dispatchers.IO){
            noteDao?.deleteAllNotes()
        }
    }

    fun getAllNotes(): LiveData<List<Note>>? {
        return noteDao?.getAllNotes()
    }
}