package com.duran.selfmg.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.duran.selfmg.data.entity.TodoListEntity
import com.duran.selfmg.data.repository.TodoListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoListVIewModel(application: Application) : AndroidViewModel(application) {

    val context = getApplication<Application>().applicationContext

    val todoRepository = TodoListRepository(context)

    private var _todoList = MutableLiveData<List<TodoListEntity>>()
    val todoList: LiveData<List<TodoListEntity>>
        get() = _todoList

    // 할 일 추가하기
    fun todoInsert(todoEntity: TodoListEntity) = viewModelScope.launch(Dispatchers.IO) {
        todoRepository.insertTodoList(todoEntity)
    }

    // 할 일 전체리스트 가져오기
    fun getAllTodoList() = viewModelScope.launch(Dispatchers.IO) {
        _todoList.postValue(todoRepository.getAllTodoList())
    }

}