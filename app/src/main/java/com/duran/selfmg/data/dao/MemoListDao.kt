package com.duran.selfmg.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.duran.selfmg.data.model.MemoListEntity

@Dao
interface MemoListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun memoInsert(memo: MemoListEntity)
}