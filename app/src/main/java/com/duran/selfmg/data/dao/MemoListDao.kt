package com.duran.selfmg.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.duran.selfmg.data.model.MemoListEntity

@Dao
interface MemoListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun memoInsert(memo: MemoListEntity)

    @Query("select * from memo_Table")
    fun getAllMemo(): LiveData<MutableList<MemoListEntity>>

    @Query("select * from memo_Table where id = (:id)")
    fun getMemo(id: Long): MemoListEntity

    @Update
    fun memoUpdate(memo: MemoListEntity)

    @Query("delete from memo_Table where id = (:id)")
    fun memoDelete(id: Long)

}