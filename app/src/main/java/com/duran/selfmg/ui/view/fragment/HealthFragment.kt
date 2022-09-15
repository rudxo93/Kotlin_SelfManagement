package com.duran.selfmg.ui.view.fragment

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.duran.selfmg.R
import com.duran.selfmg.data.model.HealthBmiEntity
import com.duran.selfmg.databinding.FragmentHealthBinding
import com.duran.selfmg.ui.view.activity.BmiCalculationActivity
import com.duran.selfmg.ui.viewmodel.HealthBmiViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

class HealthFragment : Fragment() {

    private lateinit var binding: FragmentHealthBinding
    private lateinit var auth: FirebaseAuth // 계정인증
    private lateinit var healthBmiViewModel: HealthBmiViewModel

    private var userBmi: HealthBmiEntity? = null

    private val tvBmiResultNumValue by lazy { binding.tvBmiResultNumValue }
    private val tvBmiResultRangeValue by lazy { binding.tvBmiResultRangeValue }

    private val btnBmiasdf by lazy { binding.btnBmi12312312 }

    var userBmiNum: String? = null
    var userBmiRange: String? = null

    private var isFabOpen = false // Fab 버튼 default는 닫혀있음

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_health, container, false)

        auth = FirebaseAuth.getInstance()
        healthBmiViewModel = ViewModelProvider(this)[HealthBmiViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentUser = auth.currentUser?.email

        /*val currentUser = auth.currentUser?.email
        CoroutineScope(Dispatchers.IO).launch {
            userBmi = healthBmiViewModel.getBmi(currentUser.toString())
            Log.e("tag", userBmi!!.bmiNum)
            Log.e("tag", userBmi!!.userEmail)
            Log.e("tag", userBmi!!.bmiRange)
            Log.e("tag", userBmi!!.toString())
        }
        userBmi?.let { Log.e("tag", it.bmiNum) }

        userBmiNum = userBmi?.bmiNum
        userBmiRange = userBmi?.bmiRange

        btnBmiasdf.setOnClickListener {
            tvBmiResultNumValue.setText(userBmi?.bmiNum)
            tvBmiResultRangeValue.setText(userBmi?.bmiRange)
        }*/

        setFABClickEvent()

    }

    // 플로팅 버튼 클릭 시 이벤트
    private fun setFABClickEvent() {
        // 플로팅 버튼 클릭시 애니메이션 동작 기능
        binding.fabMain.setOnClickListener {
            toggleFab()
        }

        // 플로팅 버튼 클릭 이벤트 - BMI 측정하기
        binding.fabBmi.setOnClickListener {
            Toast.makeText(this.context, "나의 BMI 측정하기", Toast.LENGTH_SHORT).show()
            val currentUser = auth.currentUser?.email

            // 현재 로그인 한 계정의 bmi정보를 조회 후 정보가 있다면 update 없다면 add
            CoroutineScope(Dispatchers.IO).launch {
                userBmi = healthBmiViewModel.getBmi(currentUser.toString())
                if(userBmi?.toString().isNullOrBlank()) {
                    Log.e("tag", "데이터가 존재하지 않습니다.")
                    val intent = Intent(activity, BmiCalculationActivity::class.java)
                    intent.putExtra("type", "AddBmi")
                    startActivity(intent)
                } else {
                    Log.e("tag", "데이터가 존재합니다.")
                    val intent = Intent(activity, BmiCalculationActivity::class.java)
                    intent.putExtra("type", "UpdateBmi")
                    intent.putExtra("item", userBmi as Serializable)
                    startActivity(intent)
                }
            }
        }

        // 플로팅 버튼 클릭 이벤트 - 물섭취량 기록하기
        binding.fabWater.setOnClickListener {
            Toast.makeText(this.context, "나의 물섭취량", Toast.LENGTH_SHORT).show()
        }
    }

    // 플로팅 버튼 -> 토글버튼으로
    private fun toggleFab() {
        // 플로팅 액션 버튼 닫기 - 열려있는 플로팅 버튼 집어넣는 애니메이션
        if (isFabOpen) {
            ObjectAnimator.ofFloat(binding.fabWater, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabBmi, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabMain, View.ROTATION, 45f, 0f).apply { start() }
        } else { // 플로팅 액션 버튼 열기 - 닫혀있는 플로팅 버튼 꺼내는 애니메이션
            ObjectAnimator.ofFloat(binding.fabWater, "translationY", -360f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabBmi, "translationY", -180f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabMain, View.ROTATION, 0f, 45f).apply { start() }
        }

        isFabOpen = !isFabOpen

    }

}