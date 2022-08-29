package com.duran.selfmg.data.repository

import android.content.Context
import com.duran.selfmg.data.db.TodoListDatabase
import com.duran.selfmg.data.entity.TodoListEntity

class TodoListRepository(context: Context) {

    private val db = TodoListDatabase.getDatabase(context)

    fun insertTodoList(todoEntity: TodoListEntity) = db.todoListDao().todoInsert(todoEntity) // 할일 추가하기

    fun getAllTodoList() = db.todoListDao().getAllTodoList() // 할일 전체 리스트 조회

}