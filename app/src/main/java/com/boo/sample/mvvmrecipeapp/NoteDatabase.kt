package com.boo.sample.mvvmrecipeapp

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KParameter

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        private var instance: NoteDatabase? = null

        @Synchronized
        fun getInstance(context: Context, scope: CoroutineScope): NoteDatabase? {
            if (instance == null) {
                synchronized(NoteDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDatabase::class.java,
                        "note-database"
                    ).fallbackToDestructiveMigration()
                        .addCallback(roomCallback(scope))
                        .build()
                }
            }
            return instance
        }

        private class roomCallback(private val scope: CoroutineScope) : RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Log.d("로그", "roomCallback onCreate 실행")

                instance?.let { noteDatabase ->
                    scope.launch {
                        populateDatabase(noteDatabase.noteDao())
                    }
                }

            }

            suspend fun populateDatabase(noteDao: NoteDao){
                noteDao.deleteAllNotes()
                noteDao.insert(Note("Title1", "Description 1", 1))
                Log.d("로그", "noteDao.insert 1번 실행")

                noteDao.insert(Note("Title2", "Description 2", 2))
                Log.d("로그", "noteDao.insert 2번 실행")
                noteDao.insert(Note("Title3", "Description 3", 3))
                Log.d("로그", "noteDao.insert 3번 실행")

            }
        }
    }
}