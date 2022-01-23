package com.boo.sample.mvvmrecipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.boo.sample.mvvmrecipeapp.databinding.ActivityMainBinding
import androidx.recyclerview.widget.RecyclerView.ViewHolder


class MainActivity : AppCompatActivity() {
    companion object {
        const val ADD_NOTE_REQUEST = 1
        const val EDIT_NOTE_REQUEST = 2
    }

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        val adapter = NoteAdapter()
        binding.apply {
            recyclerView.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
        }
        binding.buttonAddNote.setOnClickListener {
            val intent = Intent(this, AddEidtNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        noteViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )
            .get(NoteViewModel::class.java)
        noteViewModel.getAllNotes().observe(this, Observer { notes ->
            //update RecyclerView
            adapter.submitList(notes)
        })

        val mIth = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: ViewHolder, target: ViewHolder
                ): Boolean {

                    // move item in `fromPos` to `toPos` in adapter.
                    return false // true if moved, false otherwise
                }

                override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                    // remove from adapter
                    noteViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                    Toast.makeText(
                        applicationContext,
                        "note deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }).attachToRecyclerView(binding.recyclerView)

        //업데이트시
        adapter.setOnItemClickListener(object : NoteAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                val intent = Intent(applicationContext, AddEidtNoteActivity::class.java)
                intent.putExtra(AddEidtNoteActivity.EXTRA_ID, note.id)
                intent.putExtra(AddEidtNoteActivity.EXTRA_TITLE, note.title)
                intent.putExtra(AddEidtNoteActivity.EXTRA_DESCRIPTION, note.description)
                intent.putExtra(AddEidtNoteActivity.EXTRA_PRIORITY, note.priority)
                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            val title = data?.getStringExtra(AddEidtNoteActivity.EXTRA_TITLE)
            val description = data?.getStringExtra(AddEidtNoteActivity.EXTRA_DESCRIPTION)
            val priority = data?.getIntExtra(AddEidtNoteActivity.EXTRA_PRIORITY, 1)

            if (title != null && description != null && priority != null) {
                val note = Note(title, description, priority)
                noteViewModel.insert(note)
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "please fill out all blanks", Toast.LENGTH_SHORT).show()
            }

        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            val id = data?.getIntExtra(AddEidtNoteActivity.EXTRA_ID, -1)

            if(id == -1){
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show()
                return
            }
            val title = data?.getStringExtra(AddEidtNoteActivity.EXTRA_TITLE)
            val description = data?.getStringExtra(AddEidtNoteActivity.EXTRA_DESCRIPTION)
            val priority = data?.getIntExtra(AddEidtNoteActivity.EXTRA_PRIORITY, 1)

            if (title != null && description != null && priority != null){
                val note = Note(title, description, priority)
                if (id != null) {
                    note.id = id
                }
                noteViewModel.update(note)
                Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()

            }
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_notes -> {
                noteViewModel.deleteAllNotes()
                Toast.makeText(this, "All Notes deleted", Toast.LENGTH_SHORT).show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}