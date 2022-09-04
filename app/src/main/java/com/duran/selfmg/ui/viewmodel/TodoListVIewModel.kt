package com.duran.selfmg.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.duran.selfmg.data.model.TodoListEntity
import com.duran.selfmg.data.repository.TodoListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoListVIewModel(application: Application) : AndroidViewModel(application) {

    val context = getApplication<Application>().applicationContext

    private val todoRepository = TodoListRepository(context)

    val todoList: LiveData<MutableList<TodoListEntity>> = todoRepository.getAllTodoList()

    // 클릭한 할 일 content 가져오기
    fun getTodo(id: Long) = todoRepository.getTodo(id)

    // 할 일 추가하기
    fun todoInsert(todo: TodoListEntity) = viewModelScope.launch(Dispatchers.IO) {
        todoRepository.insertTodoList(todo)
    }

    // 할 일 수정하기
    fun todoUpdate(todo: TodoListEntity) = viewModelScope.launch(Dispatchers.IO) {
        todoRepository.updateTodo(todo)
    }

    // 할 일 삭제하기(단일)
    fun todoDelete(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        todoRepository.deleteTodo(id)
    }



}