package com.duran.selfmg.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "bmi_table")
data class HealthBmiEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "user_email") val userEmail: String,
    @ColumnInfo(name = "bmi_num") val bmiNum: String,
    @ColumnInfo(name = "bmi_range") var bmiRange: String
): Serializable
