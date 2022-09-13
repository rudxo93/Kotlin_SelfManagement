package com.duran.selfmg.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.duran.selfmg.data.model.HealthBmiEntity
import com.duran.selfmg.data.repository.HealthBmiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HealthBmiViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    val context = getApplication<Application>().applicationContext

    private val healthBmiRepository = HealthBmiRepository(context)

    // BMI 정보 추가하기
    fun insertBmi(bmi: HealthBmiEntity) = viewModelScope.launch(Dispatchers.IO) {
        healthBmiRepository.insertBmi(bmi)
    }

}