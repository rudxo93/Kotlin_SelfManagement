package com.duran.selfmg.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.duran.selfmg.R
import com.duran.selfmg.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    // 데이터바인딩 사용
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initSingUpBtnClicked()
    }

    private fun initSingUpBtnClicked() {

        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}