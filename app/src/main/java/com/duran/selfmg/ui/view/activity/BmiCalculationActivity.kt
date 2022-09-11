package com.duran.selfmg.ui.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.duran.selfmg.R
import com.duran.selfmg.databinding.ActivityBmiCalculationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.math.RoundingMode
import java.text.DecimalFormat

class BmiCalculationActivity : AppCompatActivity() {

    lateinit var binding: ActivityBmiCalculationBinding

    private val btnBmiInfo by lazy { binding.btnBmiInfo } // bmi에 대해서 정보보기
    private val textViewBmiInfo by lazy { binding.tvBmiInfoText } // bmi 정보 textView
    private val inputHeightText by lazy { binding.edBmiInputHeight.text } // 신장 입력
    private val inputWeightText by lazy { binding.edBmiInputWeight.text } // 체중 입력
    private val btnStartResult by lazy { binding.btnStartBmiResult } // 측정하기 버튼
    private val btnBmiClear by lazy { binding.btnStartBmiClear } // Bmi 초기화 버튼

    private var isInfoOpen = false // Bmi 정보버튼은 닫혀있다.

    private lateinit var auth: FirebaseAuth // 계정인증

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bmi_calculation)

        auth = FirebaseAuth.getInstance()

        initToolBarSetting()
        initBtnBmiInfoClick()
        initClickBtnBmiResult()
        initClickBtnClear()

        /*var authUser = auth.currentUser?.email // 현재 접속중인 유저 이메일 정보 나중에 사용*/

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
        if(isInfoOpen) {
            textViewBmiInfo.visibility = View.GONE
        } else {
            textViewBmiInfo.visibility = View.VISIBLE
        }
        isInfoOpen = !isInfoOpen
    }

    // ======================================= BMI 측정하기 버튼 클릭 =======================================
    private fun initClickBtnBmiResult() {
        btnStartResult.setOnClickListener {
            if(inputHeightText.isEmpty()) {
                Toast.makeText(this, "신장 입력창이 비었습니다.", Toast.LENGTH_SHORT).show()
            } else if (inputWeightText.isEmpty()) {
                Toast.makeText(this, "체중 입력창이 비었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                initBmiCalculationResult()
            }
        }
    }

    // ======================================= 초기화 버튼 클릭 =======================================
    private fun initClickBtnClear() {
        btnBmiClear.setOnClickListener {
            inputHeightText.clear()
            inputWeightText.clear()
            Toast.makeText(this, "신장과 체중입력을 초기화했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // ======================================= BMI수치 구하기 =======================================
    private fun initBmiCalculationResult() {
        val height = inputHeightText.toString().toFloat()
        val weight = inputWeightText.toString().toFloat()

        val bmi = weight / ((height / 100) * (height / 100)) // BMI수치 구하기

        val df = DecimalFormat("#.##") // 소숫점 둘째자리까지
        df.roundingMode = RoundingMode.DOWN

        val bmiDf = df.format(bmi)
        Log.e("tag", bmiDf.toString())
    }

}