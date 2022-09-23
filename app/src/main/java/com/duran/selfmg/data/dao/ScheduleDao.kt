package com.duran.selfmg.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duran.selfmg.data.model.ScheduleEntity

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun scheduleInsert(schedule: ScheduleEntity)

    @Query("select * from schedule_Table")
    fun getAllSchedule(): LiveData<MutableList<ScheduleEntity>>

    @Query("select * from schedule_Table where schedule_date = (:schedule_date)")
    fun getSchedule(schedule_date: String): ScheduleEntity
}