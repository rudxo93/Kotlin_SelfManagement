package com.duran.selfmg.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.duran.selfmg.data.model.ScheduleEntity
import com.duran.selfmg.data.repository.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    val context = getApplication<Application>().applicationContext

    private val scheduleRepository = ScheduleRepository(context)

    val schedule: LiveData<MutableList<ScheduleEntity>> = scheduleRepository.getAllSchedule()

    fun getSchedule(schedule_date: String) = scheduleRepository.getSchedule(schedule_date)

    fun scheduleInsert(schedule: ScheduleEntity) = viewModelScope.launch(Dispatchers.IO) {
        scheduleRepository.scheduleInsert(schedule)
    }
}