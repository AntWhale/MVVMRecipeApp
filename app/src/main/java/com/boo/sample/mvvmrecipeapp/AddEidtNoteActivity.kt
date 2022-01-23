package com.boo.sample.mvvmrecipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.NumberPicker

class AddEidtNoteActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "com.codinginflow.architectureexample.EXTRA_ID"
        const val EXTRA_TITLE = "com.codinginflow.architectureexample.EXTRA_TITLE"
        const val EXTRA_DESCRIPTION = "com.codinginflow.architectureexample.EXTRA_DESCRIPTION"
        const val EXTRA_PRIORITY = "com.codinginflow.architectureexample.EXTRA_PRIORITY"
    }

    private val editTextTitle by lazy {findViewById<EditText>(R.id.edit_text_title)}
    private val editTextDescription by lazy {findViewById<EditText>(R.id.edit_text_description)}
    private val numberPickerPriority by lazy {findViewById<NumberPicker>(R.id.number_picker_priority)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        numberPickerPriority.minValue = 1
        numberPickerPriority.maxValue = 10

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        if(intent.hasExtra(EXTRA_ID)){
            supportActionBar?.setTitle("Edit Note")
            editTextTitle.setText(intent.getStringExtra(AddEidtNoteActivity.EXTRA_TITLE))
            editTextDescription.setText(intent.getStringExtra(AddEidtNoteActivity.EXTRA_DESCRIPTION))
            numberPickerPriority.value = intent.getIntExtra(AddEidtNoteActivity.EXTRA_PRIORITY, 1)
        }else{
            supportActionBar?.setTitle("Add Note")
        }
    }

    private fun saveNote() {
        val title = editTextTitle.text?.toString()
        val description = editTextDescription.text?.toString()
        val priority = numberPickerPriority.value

        val data = Intent()
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESCRIPTION, description)
        data.putExtra(EXTRA_PRIORITY, priority)

        //업데이트 노트시
        val id = intent.getIntExtra(EXTRA_ID, -1)
        if(id != -1){
            data.putExtra(EXTRA_ID, id)
        }

        if(title == "" || description == ""){
            //값 입력하라구
            finish()
        }

        setResult(RESULT_OK, data)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_note -> {
                saveNote()
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }


}