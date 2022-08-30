package com.duran.selfmg.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duran.selfmg.R
import com.duran.selfmg.data.model.TodoListEntity

class TodoListAdapter(val context: Context) : RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    private var list = mutableListOf<TodoListEntity>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val todoContent : TextView = view.findViewById(R.id.tv_todoItem_content)
        val todoTimestamp : TextView = view.findViewById(R.id.tv_todo_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todolist_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoListAdapter.ViewHolder, position: Int) {
        holder.todoContent.text = list[position].todocontent
        holder.todoTimestamp.text = list[position].timestamp
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(newList: MutableList<TodoListEntity>) {
        this.list = newList
        notifyDataSetChanged()
    }

}
