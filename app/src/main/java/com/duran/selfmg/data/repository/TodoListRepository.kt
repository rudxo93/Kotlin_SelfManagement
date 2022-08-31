package com.duran.selfmg.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.duran.selfmg.data.db.TodoListDatabase
import com.duran.selfmg.data.model.TodoListEntity

class TodoListRepository(context: Context) {

    private val db = TodoListDatabase.getDatabase(context)

    fun insertTodoList(todo: TodoListEntity) = db.todoListDao().todoInsert(todo) // 할일 추가하기

    fun getAllTodoList(): LiveData<MutableList<TodoListEntity>> = db.todoListDao().getAllTodoList() // 할일 전체 리스트 조회

    fun getTodo(id: Long): TodoListEntity = db.todoListDao().getTodo(id)

}