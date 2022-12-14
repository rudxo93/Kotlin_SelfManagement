package com.duran.selfmg.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.duran.selfmg.data.model.MemoListEntity
import com.duran.selfmg.data.repository.MemoListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoListViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    val context = getApplication<Application>().applicationContext

    private val memoRepository = MemoListRepository(context)

    val memoList: LiveData<MutableList<MemoListEntity>> = memoRepository.getAllMemo()

    // 메모 정보 가져오기
    fun getMemo(id: Long) = memoRepository.getMemo(id)

    // 메모 추가하기
    fun memoInsert(memo: MemoListEntity) = viewModelScope.launch(Dispatchers.IO) {
        memoRepository.insertMemo(memo)
    }

    // 메모 업데이트
    fun memoUpdate(memo: MemoListEntity) = viewModelScope.launch(Dispatchers.IO) {
        memoRepository.updateMemo(memo)
    }

    // 선택한 메모 삭제하기
    fun memoDelete(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        memoRepository.deleteMemo(id)
    }
}