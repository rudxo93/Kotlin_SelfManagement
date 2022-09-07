package com.duran.selfmg.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.duran.selfmg.R
import com.duran.selfmg.data.model.MemoListEntity

class MemoListAdapter(val context: Context) : RecyclerView.Adapter<MemoListAdapter.ViewHolder>() {

    private var list = mutableListOf<MemoListEntity>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val memoTitle = itemView.findViewById<TextView>(R.id.tv_memoList_title)
        private val memoContent = itemView.findViewById<TextView>(R.id.tv_memoList_content)
        private val timestamp = itemView.findViewById<TextView>(R.id.tv_memoList_timestamp)

        fun onBind(data: MemoListEntity) {
            memoTitle.text = data.memoTitle
            memoContent.text = data.memoContent
            timestamp.text = data.timestamp
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_memolist_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newList: MutableList<MemoListEntity>) {
        this.list = newList
        notifyDataSetChanged()
    }
}