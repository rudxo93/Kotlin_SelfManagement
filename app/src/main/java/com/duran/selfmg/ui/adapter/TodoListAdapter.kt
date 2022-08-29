package com.duran.selfmg.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duran.selfmg.R
import com.duran.selfmg.data.entity.TodoListEntity

class TodoListAdapter(private var dataSet: List<TodoListEntity> ) : RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val todoContent : TextView = view.findViewById(R.id.tv_todoItem_content)
        val todoTimestamp : TextView = view.findViewById(R.id.tv_todo_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todolist_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoListAdapter.ViewHolder, position: Int) {
        holder.todoContent.text = dataSet[position].todocontent
        holder.todoTimestamp.text = dataSet[position].timestamp
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun update(newDataSet: MutableList<TodoListEntity>) {
        this.dataSet = newDataSet
        notifyDataSetChanged()
    }

}
