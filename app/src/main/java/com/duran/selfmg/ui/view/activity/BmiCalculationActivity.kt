package com.duran.selfmg.ui.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.duran.selfmg.R
import com.duran.selfmg.data.model.HealthBmiEntity
import com.duran.selfmg.databinding.ActivityBmiCalculationBinding
import com.duran.selfmg.ui.view.fragment.HealthFragment
import com.duran.selfmg.ui.viewmodel.HealthBmiViewModel
import com.duran.selfmg.ui.viewmodel.MemoListViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.round

class BmiCalculationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBmiCalculationBinding
    private lateinit var auth: FirebaseAuth // 계정인증
    private lateinit var healthBmiViewModel: HealthBmiViewModel

    private var isInfoOpen = false // Bmi 정보버튼은 닫혀있다.
    private var bmiFlag = 0

    private val btnBmiInfo by lazy { binding.btnBmiInfo } // bmi에 대해서 정보보기
    private val textViewBmiInfo by lazy { binding.tvBmiInfoText } // bmi 정보 textView
    private val inputHeightText by lazy { binding.edBmiInputHeight.text } // 신장 입력
    private val inputWeightText by lazy { binding.edBmiInputWeight.text } // 체중 입력
    private val btnStartResult by lazy { binding.btnStartBmiResult } // 측정하기 버튼
    private val btnBmiClear by lazy { binding.btnStartBmiClear } // Bmi 초기화 버튼
    private val resultLinearLayout by lazy { binding.linearBmiResult } // bmi 측정하기 클릭시 나오는 레이아웃
    private val resultBmiNum by lazy { binding.tvBmiResultNumber } // 측정된 bmi 지수
    private val resultBmiRange by lazy { binding.tvBmiResultRange } // 측정된 bmi 지수 판정
    private val btnBmiResultSave by lazy { binding.btnBmiResultSave } // 결과 저장하기 버튼

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bmi_calculation)

        healthBmiViewModel = ViewModelProvider(this)[HealthBmiViewModel::class.java]
        auth = FirebaseAuth.getInstance()

        initToolBarSetting() // 상단 toolbar
        initBtnBmiInfoClick() // 상단 BMI 정보보기 버튼 클릭
        initClickBtnBmiResult() // 측정하기 버튼
        initClickBtnClear() // 초기화 버튼
        initBtnBmiResultSave()


    }

    // ======================================= MBI 계산하기 툴바 =======================================
    private fun initToolBarSetting() {
        val toolbar = binding.toolbarBmiCalculation

        // 툴바 생성
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // ======================================= BMI 설명 보기 클릭 =======================================
    private fun initBtnBmiInfoClick() {
        btnBmiInfo.setOnClickListener {
            toggleButton()
        }
    }

    // ======================================= BMI 설명보기 버튼을 토글버튼으로 설명 숨김과 보여주기 =======================================
    private fun toggleButton() {
        if (isInfoOpen) {
            textViewBmiInfo.visibility = View.GONE
        } else {
            textViewBmiInfo.visibility = View.VISIBLE
        }
        isInfoOpen = !isInfoOpen
    }

    // ======================================= BMI 측정하기 버튼 클릭 =======================================
    private fun initClickBtnBmiResult() {
        btnStartResult.setOnClickListener {
            when {
                inputHeightText.isEmpty() -> {
                    Toast.makeText(this, "신장 입력창이 비었습니다.", Toast.LENGTH_SHORT).show()
                }
                inputWeightText.isEmpty() -> {
                    Toast.makeText(this, "체중 입력창이 비었습니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    initBmiResult()
                }
            }
        }
    }

    // ======================================= 초기화 버튼 클릭 =======================================
    private fun initClickBtnClear() {
        btnBmiClear.setOnClickListener {
            inputHeightText.clear()
            inputWeightText.clear()
            resultLinearLayout.visibility = View.GONE
            bmiFlag = 0
            Toast.makeText(this, "신장과 체중입력을 초기화했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // ======================================= BMI수치 판정 결과 =======================================
    private fun initBmiResult() {
        val height = inputHeightText.toString().toDouble()
        val weight = inputWeightText.toString().toDouble()
        val bmi = bmiCalculation(height, weight)// 계산된 BMI지수

        when {
            bmi < 18.5 -> {
                resultBmiNum.text = bmi.toString()
                resultBmiNum.setTextColor(Color.parseColor("#2D81FF"))
                resultBmiRange.text = "저체중"
                resultBmiRange.setTextColor(Color.parseColor("#2D81FF"))
                bmiFlag = 1
            }
            bmi >= 18.5 && bmi < 23 -> {
                resultBmiNum.text = bmi.toString()
                resultBmiNum.setTextColor(Color.parseColor("#2D81FF"))
                resultBmiRange.text = "정상"
                resultBmiRange.setTextColor(Color.parseColor("#2D81FF"))
                bmiFlag = 1
            }
            bmi >= 23 && bmi < 25 -> {
                resultBmiNum.text = bmi.toString()
                resultBmiNum.setTextColor(Color.parseColor("#FF000000"))
                resultBmiRange.text = "과체중"
                resultBmiRange.setTextColor(Color.parseColor("#FF000000"))
                bmiFlag = 1
            }
            bmi >= 25 && bmi < 30 -> {
                resultBmiNum.text = bmi.toString()
                resultBmiNum.setTextColor(Color.parseColor("#FF000000"))
                resultBmiRange.text = "경도비만"
                resultBmiRange.setTextColor(Color.parseColor("#FF000000"))
                bmiFlag = 1
            }
            else -> {
                resultBmiNum.text = bmi.toString()
                resultBmiNum.setTextColor(Color.parseColor("#8A3131"))
                resultBmiRange.text = "중등도비만"
                resultBmiRange.setTextColor(Color.parseColor("#8A3131"))
                bmiFlag = 1
            }

        }

        resultLinearLayout.visibility = View.VISIBLE
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

    }

    // ======================================= BMI수치 구하기 =======================================
    private fun bmiCalculation(height: Double, weight: Double): Double {
        val BMI = weight / ((height / 100) * (height / 100)) // BMI수치 구하기

        val BMIResult = round(BMI * 100) / 100

        return BMIResult
    }

    // ======================================= BMI수치 결과 저장하기 버튼 클릭 =======================================
    private fun initBtnBmiResultSave() {
        btnBmiResultSave.setOnClickListener {
            Toast.makeText(this, "BMI정보를 저장합니다.", Toast.LENGTH_SHORT).show()
            when {
                inputHeightText.isEmpty() -> {
                    Toast.makeText(this, "신장 입력창이 비었습니다.", Toast.LENGTH_SHORT).show()
                }
                inputWeightText.isEmpty() -> {
                    Toast.makeText(this, "체중 입력창이 비었습니다.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    initSaveFlagCheck()
                }
            }
        }
    }

    // ======================================= BMI수치 결과 Flag값에 따라서 분류 =======================================
    private fun initSaveFlagCheck() {
        when(bmiFlag) {
            1 -> { // 측정하기 버튼 클릭으로 측정한 데이터가 존재
                initSaveBmiResult()
            }
            else -> { // Flag가 0이라면 초기화를 눌러서 측정한 값이 사라짐 또는 측정 x
                Toast.makeText(this, "BMI를 먼저 측정 후 저장하기를 눌러주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ======================================= BMI수치 결과 Flag값에 따라서 분류 =======================================
    private fun initSaveBmiResult() {
        val bmiNum = resultBmiNum.text
        val bmiRange = resultBmiRange.text
        var authUser = auth.currentUser?.email // 현재 접속중인 유저 이메일 정보

        CoroutineScope(Dispatchers.IO).launch {
            val healthBmiEntity = HealthBmiEntity(0, authUser.toString(), bmiNum.toString(), bmiRange.toString())
            healthBmiViewModel.insertBmi(healthBmiEntity)
        }
        Toast.makeText(this, "측정하신 BMI정보가 저장되었습니다.", Toast.LENGTH_SHORT).show()

        finish()
    }


}