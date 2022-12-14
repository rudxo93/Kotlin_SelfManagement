package com.duran.selfmg.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.duran.selfmg.data.db.SelfMgDatabase
import com.duran.selfmg.data.model.TodoListEntity

class TodoListRepository(context: Context) {

    private val db = SelfMgDatabase.getDatabase(context)

    // 할일 추가하기
    fun insertTodoList(todo: TodoListEntity) = db.todoListDao().todoInsert(todo)

    // 할일 전체 리스트 조회
    fun getAllTodoList(): LiveData<MutableList<TodoListEntity>> = db.todoListDao().getAllTodoList()

    // 할 일 하나만 가져오기
    fun getTodo(id: Long): TodoListEntity = db.todoListDao().getTodo(id)

    // 할 일 수정하기
    fun updateTodo(todo: TodoListEntity) = db.todoListDao().todoUpdate(todo)

    // 할 일 삭제하기(단일)
    fun deleteTodo(id: Long) = db.todoListDao().todoDelete(id)

    // 선택한 할 일 삭제하기
    fun selectTodoDeleteAll(todo: TodoListEntity) = db.todoListDao().selectTodoDeleteAll(todo)

}