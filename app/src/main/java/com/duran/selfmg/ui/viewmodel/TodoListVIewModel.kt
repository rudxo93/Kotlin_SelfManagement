package com.duran.selfmg.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.duran.selfmg.data.db.TodoListDatabase
import com.duran.selfmg.data.entity.TodoListEntity
import com.duran.selfmg.data.repository.TodoListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoListVIewModel(application: Application) : AndroidViewModel(application) {

    val context = getApplication<Application>().applicationContext

    val todoRepository = TodoListRepository(context)

    private var _todoList = MutableLiveData<List<TodoListEntity>>()
    val todoList : LiveData<List<TodoListEntity>>
        get() = _todoList


    fun todoInsert(todoDao: TodoListEntity) = viewModelScope.launch(Dispatchers.IO) {
        todoRepository.insertTodoList(todoDao)
    }

}