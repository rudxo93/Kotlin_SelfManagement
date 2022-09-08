package com.duran.selfmg.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.duran.selfmg.data.db.SelfMgDatabase
import com.duran.selfmg.data.model.MemoListEntity

class MemoListRepository(context: Context) {

    private val db = SelfMgDatabase.getDatabase(context)

    // 메모 추가하기
    fun insertMemo(memo: MemoListEntity) = db.memoListDao().memoInsert(memo)

    // 전체 메모 조회
    fun getAllMemo(): LiveData<MutableList<MemoListEntity>> = db.memoListDao().getAllMemo()

    // 해당 메모 조회
    fun getMemo(id: Long) = db.memoListDao().getMemo(id)

}