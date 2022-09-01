package com.duran.selfmg.ui.adapter

import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duran.selfmg.R
import com.duran.selfmg.data.model.TodoListEntity

class TodoListAdapter(val context: Context) : RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    private var list = mutableListOf<TodoListEntity>()

    private lateinit var itemClickListner: ItemClickListener

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todoContent : TextView = itemView.findViewById(R.id.tv_todoItem_content)
        val todoTimestamp : TextView = itemView.findViewById(R.id.tv_todo_timestamp)
        val checkbox : CheckBox = itemView.findViewById(R.id.cb_todoCheck)

        fun onBind(data: TodoListEntity) {
            todoContent.text = data.todocontent
            todoTimestamp.text = data.timestamp
            checkbox.isChecked = data.isChecked

            if(data.isChecked) {
                todoContent.paintFlags = todoContent.paintFlags or STRIKE_THRU_TEXT_FLAG
            } else {
                todoContent.paintFlags = todoContent.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
            }

            itemView.setOnClickListener {
                itemClickListner.onClick(it, layoutPosition, list[layoutPosition].id)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todolist_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoListAdapter.ViewHolder, position: Int) {
        holder.onBind(list[position])
        holder.itemView.setOnClickListener {
            itemClickListner.onClick(it, position, list[position].id)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun update(newList: MutableList<TodoListEntity>) {
        this.list = newList
        notifyDataSetChanged()
    }

    private lateinit var itemCheckBoxClickListener: ItemCheckBoxClickListener

    interface ItemCheckBoxClickListener {
        fun onClick(view: View, position: Int, itemId: Long)
    }

    fun setItemCheckBoxClickListener(itemCheckBoxClickListener: ItemCheckBoxClickListener) {
        this.itemCheckBoxClickListener = itemCheckBoxClickListener
    }

    interface ItemClickListener {
        fun onClick(view: View, position: Int, itemId: Long)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }
}
