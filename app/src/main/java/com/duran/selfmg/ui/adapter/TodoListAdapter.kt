package com.duran.selfmg.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duran.selfmg.R
import com.duran.selfmg.data.model.TodoListEntity

class TodoListAdapter(val context: Context) : RecyclerView.Adapter<TodoListAdapter.ViewHolder>() {

    private var list = mutableListOf<TodoListEntity>()

    private lateinit var itemClickListner: ItemClickListener
    private lateinit var itemCheckBoxClickListener: ItemCheckBoxClickListener
    private lateinit var itemDeleteImageClickListener: ItemDeleteImageClickListener

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val todoContent = itemView.findViewById<TextView>(R.id.tv_todoItem_content)
        private val todoTimestamp = itemView.findViewById<TextView>(R.id.tv_todo_timestamp)
        val todoListBox = itemView.findViewById<LinearLayout>(R.id.linear_todo)!!
        val checkbox = itemView.findViewById<ImageView>(R.id.iv_todoCheck)!!
        val todoDeleteIcon = itemView.findViewById<ImageView>(R.id.iv_todo_delete)!!

        fun onBind(data: TodoListEntity) {
            todoContent.text = data.todocontent
            todoTimestamp.text = data.timestamp

            // checked 토글 동작
           if(!data.isChecked) {
               checkbox.setImageResource(R.drawable.ic_round_circle) // 체크박스 이미지 변경
               todoDeleteIcon.visibility = View.GONE // delete이미지 숨기기
               // content와 time 텍스트 색 변경
               todoContent.setTextColor(Color.BLACK)
               todoTimestamp.setTextColor(Color.BLACK)
               // content와 time 클릭 가능
               todoContent.isClickable = false
               todoContent.isFocusable = false
               todoTimestamp.isClickable = false
               todoTimestamp.isFocusable = false
           } else {
               checkbox.setImageResource(R.drawable.ic_check_circle) // 체크박스 이미지 변경
               todoDeleteIcon.visibility = View.VISIBLE // delete이미지 보이게하기
               // content와 time 텍스트 색 변경
               todoContent.setTextColor(Color.GRAY)
               todoTimestamp.setTextColor(Color.GRAY)
               // content와 time 클릭 방지
               todoContent.isClickable = true
               todoContent.isFocusable = true
               todoTimestamp.isClickable = true
               todoTimestamp.isFocusable = true
           }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todolist_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoListAdapter.ViewHolder, position: Int) {
        holder.onBind(list[position])

        // todo컨텐츠 클릭 이벤트
        holder.todoListBox.setOnClickListener {
            itemClickListner.onClick(it, position, list[position].id)
        }

        // 체크박스 클릭 이벤트
        holder.checkbox.setOnClickListener {
            itemCheckBoxClickListener.onClick(it, position, list[position].id)
        }

        // todo단일 삭제 버튼 클릭 이벤트
        holder.todoDeleteIcon.setOnClickListener {
            itemDeleteImageClickListener.onClick(it, position, list[position].id)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newList: MutableList<TodoListEntity>) {
        this.list = newList
        notifyDataSetChanged()
    }

    // ======================================= 컨텐츠 클릭 이벤트 =======================================
    interface ItemClickListener {
        fun onClick(view: View, position: Int, itemId: Long)
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListner = itemClickListener
    }

    // ======================================= 체크박스 이미지 클릭 이벤트 =======================================
    interface ItemCheckBoxClickListener {
        fun onClick(view: View, position: Int, itemId: Long)
    }

    fun setItemCheckBoxClickListener(itemCheckBoxClickListener: ItemCheckBoxClickListener) {
        this.itemCheckBoxClickListener = itemCheckBoxClickListener
    }

    // ======================================= 할 일 삭제하기 이미지 클릭 이벤트 =======================================
    interface ItemDeleteImageClickListener {
        fun onClick(view: View, position: Int, itemId: Long)
    }

    fun setItemDeleteImageClickListener(itemDeleteImageClickListener: ItemDeleteImageClickListener) {
        this.itemDeleteImageClickListener = itemDeleteImageClickListener
    }
}
