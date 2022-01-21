package com.boo.sample.mvvmrecipeapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note (
    @PrimaryKey(autoGenerate = true)
    private var id: Int,
    private val title: String,
    private val description: String,
    private val priority: Int)