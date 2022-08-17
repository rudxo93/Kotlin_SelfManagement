package com.duran.selfmg.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.duran.selfmg.R
import com.duran.selfmg.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
        // 비밀번호 입력
        initPassword()
        // 비밀번호 입력 확인
        initPasswordCheck()
        // 회원가입 버튼 클릭
        binding.btnSignupFinish.setOnClickListener {
            initSignUp()
        }
    }

    // =======================================이메일=======================================
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
                    // 이메일 입력이 비었다면 -> 이메일 입력해주세요 문구
                    binding.liEmailCheckAlarm.visibility = View.VISIBLE
                    binding.ivEmailAlarm.setImageResource(R.drawable.ic_cancel)
                    binding.tvEmailAlarm.text = "이메일을 입력해주세요"
                } else if(pattern.matcher(email).matches()) {
                    // 이메일 패턴을 가져와서 패턴이 맞다면
                    binding.liEmailCheckAlarm.visibility = View.VISIBLE
                    binding.ivEmailAlarm.setImageResource(R.drawable.ic_check_circle)
                    binding.tvEmailAlarm.text = "사용가능한 이메일입니다."
                } else {
                    // 이메일 입력이 되어있고 패턴이 틀리다면
                    binding.liEmailCheckAlarm.visibility = View.VISIBLE
                    binding.ivEmailAlarm.setImageResource(R.drawable.ic_cancel)
                    binding.tvEmailAlarm.text = "이메일 형식이 맞지 않습니다."
                }
            }

            // 입력이 끝났을 때
            override fun afterTextChanged(s: Editable?) {

            }

        })
        setListenerToEmailEditText()

    }

    /* 이메일 엔터키 클릭 시 키패드 내리기 */
    private fun setListenerToEmailEditText() {
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
    // =======================================이메일=======================================

    // =======================================비밀번호=======================================
    /* 비밀번호 입력 */
    private fun initPassword() {
        binding.edCreatePassword.addTextChangedListener(object: TextWatcher {
            // 비밀번호 정규식
            val pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,20}$" // 영문, 숫자, 특수문자 8 ~ 20자 패턴
            val pattern = Pattern.compile(pwPattern)
            val pw = binding.edCreatePassword.text

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(pw.isEmpty()){
                    // 비밀번호 입력이 비어있다면 -> 정규식대로 입력하라고 메세지
                    binding.liPw.visibility = View.VISIBLE
                    binding.ivPwAlarm.setImageResource(R.drawable.ic_cancel)
                    binding.tvPwAlarm.text = "비밀번호를 입력해주세요"
                } else if(pattern.matcher(pw).matches()) {
                    // 비밀번호 정규식 패턴이 맞다면
                    binding.liPw.visibility = View.VISIBLE
                    binding.ivPwAlarm.setImageResource(R.drawable.ic_check_circle)
                    binding.tvPwAlarm.text = "사용 가능한 비밀번호입니다."
                } else {
                    // 비밀번호 입력이 되어있고 정규식 패턴이 틀리다면
                    binding.liPw.visibility = View.VISIBLE
                    binding.ivPwAlarm.setImageResource(R.drawable.ic_cancel)
                    binding.tvPwAlarm.text = "비밀번호는 영문, 숫자, 특수문자 포함 8~20자입니다."
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        setListenerToPwEditText()
    }

    /* 비밀번호 입력 엔터키 클릭 시 키패드 내리기 */
    private fun setListenerToPwEditText() {
        binding.edCreatePassword.setOnKeyListener { v, keyCode, event ->
            // 엔터키 액션
            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                // 키패드 내리기
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.edCreatePassword.windowToken, 0)
                true
            }
            false
        }
    }
    // =======================================비밀번호=======================================

    // =======================================비밀번호 확인=======================================
    private fun initPasswordCheck() {
        //
        binding.edCreatePasswordCheck.addTextChangedListener(object: TextWatcher {
            // 비밀번호 정규식
            val pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,20}$" // 영문, 숫자, 특수문자 8 ~ 20자 패턴
            val pattern = Pattern.compile(pwPattern)
            val pwCheck = binding.edCreatePasswordCheck.text
            val pw = binding.edCreatePassword.text

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(pwCheck.isEmpty()){
                    // 비밀번호 확인이 비어있다면 -> 정규식대로 입력하라고 메세지
                    binding.liPwCheck.visibility = View.VISIBLE
                    binding.ivPwCheckAlarm.setImageResource(R.drawable.ic_cancel)
                    binding.tvPwCheckAlarm.text = "비밀번호를 입력해주세요"
                } else if(pattern.matcher(pwCheck).matches()) {
                    // 비밀번호 정규식 패턴이 맞다면 -> 비밀번호가 일치하는지?
                    if(pw.toString().equals(pwCheck.toString())) {
                        binding.liPwCheck.visibility = View.VISIBLE
                        binding.ivPwCheckAlarm.setImageResource(R.drawable.ic_check_circle)
                        binding.tvPwCheckAlarm.text = "비밀번호가 일치합니다."
                    } else { // 위와 비밀번호가 일치하지 않다면?
                        binding.liPwCheck.visibility = View.VISIBLE
                        binding.ivPwCheckAlarm.setImageResource(R.drawable.ic_cancel)
                        binding.tvPwCheckAlarm.text = "비밀번호가 일치하지 않습니다."
                    }
                } else {
                    // 비밀번호 입력이 되어있고 정규식 패턴이 틀리다면
                    binding.liPwCheck.visibility = View.VISIBLE
                    binding.ivPwCheckAlarm.setImageResource(R.drawable.ic_cancel)
                    binding.tvPwCheckAlarm.text = "비밀번호는 영문, 숫자, 특수문자 포함 8~20자입니다."
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        setListenerToPwCheckEditText()
    }

    /* 비밀번호 확인 엔터키 클릭 시 키패드 내리기 */
    private fun setListenerToPwCheckEditText() {
        binding.edCreatePasswordCheck.setOnKeyListener { v, keyCode, event ->
            // 엔터키 액션
            if(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                // 키패드 내리기
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.edCreatePasswordCheck.windowToken, 0)
                true
            }
            false
        }
    }
    // =======================================비밀번호 확인=======================================

    // =======================================회원가입 완료=======================================
    private fun initSignUp() {
        val email = binding.edUsedEmail.text.toString()
        val pw = binding.edCreatePassword.text.toString()
        val pwCheck = binding.edCreatePasswordCheck.text

        /*Log.e("ff", binding.edUsedEmail.text.toString())
        Log.e("ff", auth.currentUser?.email.toString())*/

        if(email.isEmpty()) { // 이메일 입력이 비었다면
            Toast.makeText(this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show()
            binding.liEmailCheckAlarm.visibility = View.VISIBLE
            binding.ivEmailAlarm.setImageResource(R.drawable.ic_cancel)
            binding.tvEmailAlarm.text = "이메일을 입력해주세요"
        } else if(pw.isEmpty()){ // 비밀번호 입력이 비었다면
            Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            binding.liPwCheck.visibility = View.VISIBLE
            binding.ivPwCheckAlarm.setImageResource(R.drawable.ic_cancel)
            binding.tvPwCheckAlarm.text = "비밀번호를 입력해주세요"
        } else if(pwCheck.isEmpty()){ // 비밀번호 확인 입력이 비었다면
            Toast.makeText(this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
            binding.liPwCheck.visibility = View.VISIBLE
            binding.ivPwCheckAlarm.setImageResource(R.drawable.ic_cancel)
            binding.tvPwCheckAlarm.text = "비밀번호를 입력해주세요"
        } else {
            auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener {
                    task ->
                if(task.isSuccessful) {
                    // 신규계정 생성
                    Toast.makeText(this, "회원가입이 완료되었습니다. 로그인 해주세요.", Toast.LENGTH_SHORT).show()
                    moveLogin(task.result?.user)
                } else {
                    // 이미 계정이 존재한다면?
                    Toast.makeText(this, "이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show()
                    binding.liEmailCheckAlarm.visibility = View.VISIBLE
                    binding.ivEmailAlarm.setImageResource(R.drawable.ic_cancel)
                    binding.tvEmailAlarm.text = "이미 존재하는 이메일입니다."
                }
            }
        }
    }
    // =======================================회원가입 완료=======================================

    /* 회원가입 성공 시 Login 페이지로 이동 */
    private fun moveLogin(user: FirebaseUser?) {
        if(user != null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    /* 회원가입 성공 시 Login 페이지로 이동 */

    // =======================================회원가입 툴바=======================================
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
    // =======================================회원가입 툴바=======================================
}