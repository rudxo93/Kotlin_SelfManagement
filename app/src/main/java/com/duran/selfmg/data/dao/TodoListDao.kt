package com.duran.selfmg.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.duran.selfmg.data.model.TodoListEntity

@Dao
interface TodoListDao {

    // TodoList 추가하기
    @Insert(onConflict = OnConflictStrategy.REPLACE) // 충돌 처리방식 -> REPLACE 충돌이 발생할 경우 덮어쓰기
    fun todoInsert(todo: TodoListEntity) // 비동기 작업 suspend

    @Query("select * from todo_Table")
    fun getAllTodoList(): LiveData<MutableList<TodoListEntity>>

    @Query("select * from todo_Table where id = (:id)")
    fun getTodo(id: Long): TodoListEntity

    @Update
    fun todoUpdate(todo: TodoListEntity)

    @Query("delete from todo_Table where id = (:id)")
    fun todoDelete(id: Long)

    @Delete
    fun selectTodoDeleteAll(todo: TodoListEntity)

}