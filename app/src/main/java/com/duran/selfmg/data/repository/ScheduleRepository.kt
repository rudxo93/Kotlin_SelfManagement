package com.duran.selfmg.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.duran.selfmg.data.db.SelfMgDatabase
import com.duran.selfmg.data.model.ScheduleEntity

class ScheduleRepository(context: Context) {

    private val db = SelfMgDatabase.getDatabase(context)

    fun scheduleInsert(schedule: ScheduleEntity) = db.scheduleDao().scheduleInsert(schedule)

    fun getAllSchedule(): LiveData<MutableList<ScheduleEntity>> = db.scheduleDao().getAllSchedule()

    fun getSchedule(schedule_date: String) = db.scheduleDao().getSchedule(schedule_date)

}