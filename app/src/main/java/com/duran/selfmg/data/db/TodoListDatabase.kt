package com.duran.selfmg.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.duran.selfmg.data.dao.TodoListDao
import com.duran.selfmg.data.model.TodoListEntity

@Database(entities = [TodoListEntity::class], version = 1)
abstract class TodoListDatabase : RoomDatabase() {

    abstract fun todoListDao(): TodoListDao

    companion object {
        @Volatile
        private var INSTANCE: TodoListDatabase? = null

        fun getDatabase(context: Context): TodoListDatabase {
            return INSTANCE ?: synchronized(this) {
                var instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoListDatabase::class.java,
                    "todo_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}