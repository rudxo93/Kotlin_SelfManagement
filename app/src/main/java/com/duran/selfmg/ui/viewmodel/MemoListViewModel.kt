package com.duran.selfmg.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.duran.selfmg.data.model.MemoListEntity
import com.duran.selfmg.data.repository.MemoListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoListViewModel(application: Application) : AndroidViewModel(application) {

    val context = getApplication<Application>().applicationContext

    private val memoRepository = MemoListRepository(context)

    // 메모 추가하기
    fun memoInsert(memo: MemoListEntity) = viewModelScope.launch(Dispatchers.IO) {
        memoRepository.insertMemo(memo)
    }

}