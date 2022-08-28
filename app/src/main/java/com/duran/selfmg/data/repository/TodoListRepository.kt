package com.duran.selfmg.data.repository

import android.content.Context
import com.duran.selfmg.data.db.TodoListDatabase
import com.duran.selfmg.data.entity.TodoListEntity

class TodoListRepository(context: Context) {

    private val db = TodoListDatabase.getDatabase(context)


    fun insertTodoList(todoDao: TodoListEntity) = db.todoListDao().todoInsert(todoDao)

}