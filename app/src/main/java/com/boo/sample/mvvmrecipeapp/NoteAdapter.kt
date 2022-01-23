package com.boo.sample.mvvmrecipeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.boo.sample.mvvmrecipeapp.databinding.NoteItemBinding

class NoteAdapter : ListAdapter<Note, NoteAdapter.NoteHolder>(diffUtil) {
    //private var notes : List<Note> = ArrayList()
    private lateinit var listener: OnItemClickListener

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Note>(){
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.title.equals(newItem.title) &&
                        oldItem.description.equals(newItem.description) &&
                        oldItem.priority == newItem.priority
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        return NoteHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.note_item, parent, false))
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        // val note = notes[position]
        holder.bind(getItem(position))

    }

    /*override fun getItemCount(): Int {
        return notes.size
    }

    fun setNotes(notes: List<Note>){
        this.notes = notes
        notifyDataSetChanged()

    }
*/
    fun getNoteAt(position: Int): Note{
        return getItem(position)
    }

    inner class NoteHolder(val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note){
            binding.apply {
                textViewTitle.text = note.title
                textViewPriority.text = note.priority.toString()
                textViewDescription.text = note.description
                itemView.setOnClickListener{
                    listener?.onItemClick(note)
                }
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}