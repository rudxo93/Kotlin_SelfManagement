package com.duran.selfmg.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.duran.selfmg.data.model.HealthBmiEntity
import com.duran.selfmg.data.repository.HealthBmiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
enum class ActionType {
    haveUser, firstUser
}
*/

class HealthBmiViewModel(application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    val context = getApplication<Application>().applicationContext

    private val healthBmiRepository = HealthBmiRepository(context)

    // BMI 정보 추가하기
    fun insertBmi(bmi: HealthBmiEntity) = viewModelScope.launch(Dispatchers.IO) {
        healthBmiRepository.insertBmi(bmi)
    }

    fun getBmi(userEmail: String) = healthBmiRepository.getBmi(userEmail)

}