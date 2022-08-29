package com.duran.selfmg.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duran.selfmg.data.entity.TodoListEntity

@Dao
interface TodoListDao {

    // TodoList 추가하기
    @Insert(onConflict = OnConflictStrategy.REPLACE) // 충돌 처리방식 -> REPLACE 충돌이 발생할 경우 덮어쓰기
    fun todoInsert(todoEntity: TodoListEntity)

    @Query("select * from todoListTable")
    fun getAllTodoList(): List<TodoListEntity>

}