package com.duran.selfmg.data.repository

import android.content.Context
import com.duran.selfmg.data.db.SelfMgDatabase
import com.duran.selfmg.data.model.HealthBmiEntity

class HealthBmiRepository(context: Context) {

    private val db = SelfMgDatabase.getDatabase(context)

    // BMI정보 추가하기
    fun insertBmi(bmi: HealthBmiEntity) = db.healthBmiDao().InsertBmi(bmi)

}