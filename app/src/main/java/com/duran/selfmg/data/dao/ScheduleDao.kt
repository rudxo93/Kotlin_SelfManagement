package com.duran.selfmg.data.dao

import androidx.room.*
import com.duran.selfmg.data.model.ScheduleEntity

@Dao
interface ScheduleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun scheduleInsert(schedule: ScheduleEntity)

    @Query("select * from schedule_Table where schedule_date = (:schedule_date)")
    fun getSchedule(schedule_date: String): ScheduleEntity

    @Update
    fun scheduleUpdate(schedule: ScheduleEntity)

    @Query("delete from schedule_Table where schedule_date = (:schedule_date)")
    fun scheduleDelete(schedule_date: String)
}