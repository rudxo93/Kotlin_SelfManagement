package com.duran.selfmg.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.duran.selfmg.data.model.HealthBmiEntity

@Dao
interface HealthBmiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun InsertBmi(bmi: HealthBmiEntity)

}