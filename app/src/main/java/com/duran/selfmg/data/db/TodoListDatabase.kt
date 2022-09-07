package com.duran.selfmg.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.duran.selfmg.data.dao.MemoListDao
import com.duran.selfmg.data.dao.TodoListDao
import com.duran.selfmg.data.model.MemoListEntity
import com.duran.selfmg.data.model.TodoListEntity

@Database(entities = [TodoListEntity::class, MemoListEntity::class], version = 2)
abstract class SelfMgDatabase : RoomDatabase() {

    abstract fun todoListDao(): TodoListDao
    abstract fun memoListDao(): MemoListDao

    companion object {
        @Volatile
        private var INSTANCE: SelfMgDatabase? = null

        fun getDatabase(context: Context): SelfMgDatabase {
            return INSTANCE ?: synchronized(this) {
                var instance = Room.databaseBuilder(
                    context.applicationContext,
                    SelfMgDatabase::class.java,
                    "selfMg_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}