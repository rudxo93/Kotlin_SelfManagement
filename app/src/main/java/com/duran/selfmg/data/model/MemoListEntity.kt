package com.duran.selfmg.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "memo_Table")
data class MemoListEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "memo_title") val memoTitle: String,
    @ColumnInfo(name = "memo_content") val memoContent: String,
    @ColumnInfo(name = "timestamp") val timestamp: String,
    @ColumnInfo(name = "isChecked") var isChecked: Boolean
): Serializable