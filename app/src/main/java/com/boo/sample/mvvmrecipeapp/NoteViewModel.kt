package com.boo.sample.mvvmrecipeapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private var repository: NoteRepository
    private var allNotes: LiveData<List<Note>>

    init {
        repository = NoteRepository(application)
        allNotes = repository.getAllNotes()!!
    }

    fun insert(note: Note){
        viewModelScope.launch {
            repository.insert(note)
        }
    }

    fun update(note: Note){
        viewModelScope.launch {
            repository.update(note)
        }

    }

    fun delete(note: Note){
        viewModelScope.launch {
            repository.delete(note)
        }
    }

    fun deleteAllNotes(){
        viewModelScope.launch {
            repository.deleteAllNotes()
        }

    }

    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes
    }
}