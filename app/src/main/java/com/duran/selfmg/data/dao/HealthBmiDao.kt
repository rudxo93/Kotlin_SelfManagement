package com.duran.selfmg.data.dao

import androidx.room.*
import com.duran.selfmg.data.model.HealthBmiEntity

@Dao
interface HealthBmiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBmi(bmi: HealthBmiEntity)

    @Query("select * from bmi_table where user_email = (:userEmail)")
    fun getBmi(userEmail: String): HealthBmiEntity

    @Update
    fun updateBmi(bmi: HealthBmiEntity)

}