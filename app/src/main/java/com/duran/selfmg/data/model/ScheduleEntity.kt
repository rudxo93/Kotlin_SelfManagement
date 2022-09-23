package com.duran.selfmg.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "schedule_Table")
data class ScheduleEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "schedule_date") val scheduleDate: String,
    @ColumnInfo(name = "schedule_content") val scheduleContent: String,
): Serializable