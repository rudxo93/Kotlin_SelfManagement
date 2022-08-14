package com.duran.selfmg.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.duran.selfmg.R
import com.duran.selfmg.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    // 계정 인증
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        initToolBarSetting()
        // 이메일 입력
        initEmail()
        // 이메일 입력 시 엔터키 클릭시 패드 숨기기
        setListenerToEditText()

    }

    /* 이메일 입력 */
    private fun initEmail() {
        binding.edUsedEmail.addTextChangedListener(object: TextWatcher {
            val pattern: Pattern = android.util.Patterns.EMAIL_ADDRESS
            val email = binding.edUsedEmail.text

            // 입력하기 전
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            // 텍스트 변화가 있을 시
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 우선 이메일 입력이 비었는지 구문
                if(email.isEmpty()){
                    binding.liEmailCheckAlarm.visibility = View.VISIBLE
                    binding.ivEmailAlarm.setImageResource(R.drawable.ic_cancel)
                    binding.tvEmailAlarm.text = "이메일을 입력해주세요"
                } else if(pattern.matcher(email).matches()) {
                    binding.liEmailCheckAlarm.visibility = View.VISIBLE
                    binding.ivEmailAlarm.setImageResource(R.drawable.ic_check_circle)
                    binding.tvEmailAlarm.text = "성공"
                } else {
                    binding.liEmailCheckAlarm.visibility = View.VISIBLE
                    binding.ivEmailAlarm.setImageResource(R.drawable.ic_cancel)
                    binding.tvEmailAlarm.text = "이메일 형식이 맞지 않습니다."
                }
            }

            // 입력이 끝났을 때
            override fun afterTextChanged(s: Editable?) {

            }

        })

    }

    /* 이메일 엔터키 클릭 시 키패드 내리기 */
    private fun setListenerToEditText() {
        binding.edUsedEmail.setOnKeyListener { _, keyCode, event ->
            // 엔터키 액션
            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                // 키패드 내리기
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.edUsedEmail.windowToken, 0)
                true
            }
            false
        }

    }
    /* 이메일 엔터키 클릭 시 키패드 내리기 */

    /* 회원가입 성공 시 Login 페이지로 이동 */
    private fun moveLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
    /* 회원가입 성공 시 Login 페이지로 이동 */

    /* toolbar */
    private fun initToolBarSetting() {
        val toolbar = binding.toolbar

        // 툴바 생성
       setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when(id) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    /* toolbar */
}