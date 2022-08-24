package com.duran.selfmg.ui.view

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.duran.selfmg.R
import com.duran.selfmg.data.model.UserModel
import com.duran.selfmg.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    lateinit var auth: FirebaseAuth // 계정 인증
    lateinit var firestore: FirebaseFirestore// DB

    private val signUpFinishBtn by lazy { binding.btnSignupFinish }
    // 이메일
    private val email by lazy { binding.edUsedEmail }
    private val btnEmailCk by lazy { binding.btnUsedEmailCheck }
    // 비밀번호
    private val pw by lazy { binding.edCreatePassword }
    // 비밀번호 체크
    private val pwCk by lazy { binding.edCreatePasswordCheck }
    private val btnPwCk by lazy { binding.btnPasswordCheck }

    var isEmailSuccess = 0 // 이메일 중복 구분
    var isPwSuccess = 0 // 비밀번호 일치 확인 구분

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        initToolBarSetting() // 상단 툴바
        initBtnEmailAvailable() // 이메일 중복확인
        initBtnPwCheck() // 비밀번호 일치 확인
        initBtnSignUp() // 회원가입 완료 버튼

    }

    // =======================================회원가입 툴바=======================================
    private fun initToolBarSetting() {
        val toolbar = binding.toolbar

        // 툴바 생성
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            android.R.id.home -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // =======================================이메일 중복확인 버튼 클릭=======================================
    private fun initBtnEmailAvailable() {
        /* 이메일 중복확인 버튼클릭
           1. 이메일 입력칸이 비었는지 확인
           2. 이메일 패턴확인
           3. DB에 존재하는 이메일과 중복인지 확인 */
        btnEmailCk.setOnClickListener {
            // 이메일이 비었는지 구분
            if(email.text.isNotEmpty()){
                // 이메일 입력함 -> 패턴 확인
                initEmailPatternCk()
            } else {
                initEmailEmpty()
            }
        }
    }

    // 이메일 패턴 확인
    private fun initEmailPatternCk () {
        val pattern: Pattern = android.util.Patterns.EMAIL_ADDRESS // 이메일 패턴
        if(pattern.matcher(email.text).matches()) {
            // 패턴일치 -> DB 중복확인
            initEmailCk()
        } else {
            initPatternErrorEmail()
        }
    }

    // 이메일 중복 확인
    private fun initEmailCk() {
        val useEmail = email.text.toString()
        // user컬렉션에서 userId가 현재 가입하고자 하는 이메일이 있는지 조회
        firestore.collection("user").whereEqualTo("userId", useEmail).get()
            .addOnSuccessListener {
                result ->
                for(item in result.documentChanges) { // 같은 이메일이 있다.
                    initUnavailableEmail()
                    email.text.clear()
                }
                if(result.size() == 0) { // 같은 이메일이 없다
                    initAvailableEmail()
                    email.isEnabled = false // 중복 확인 후 변경방치 readonly
                }
            }
    }

    // =======================================비밀번호 일치확인 버튼 클릭=======================================
    private fun initBtnPwCheck() {
        /* 비밀번호가 둘다 일치하는지 버튼 클릭
           1. 비밀번호 입력칸이 비었는지 확인
           2. 비밀번호 패턴 확인
           3. 비밀번호가 일치한지 확인*/
        btnPwCk.setOnClickListener {
            if(pw.text.isEmpty()) {
                initPwEmpty()
            } else if(pwCk.text.isEmpty()) {
                initPwCkEmpty()
            } else {
                // 비밀번호 둘다 입력함 -> 패턴 확인
                initPwPatternCk()
            }
        }
    }

    // 비밀번호 패턴 확인
    private fun initPwPatternCk() {
        // 비밀번호 정규식
        val pwPattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&.])[A-Za-z[0-9]$@$!%*#?&.]{8,20}$" // 영문, 숫자, 특수문자 8 ~ 20자 패턴
        val pattern = Pattern.compile(pwPattern)

        if(!pattern.matcher(pw.text).matches()) { // 비밀번호 패턴이 틀리다면
            initPatternErrorPw()
        } else if (!pattern.matcher(pwCk.text).matches()) {
            initPatternErrorPwCk()
        } else {
            initPwSameCk()
        }
    }

    // 비밀번호 일치 확인
    private fun initPwSameCk() {
        // 비밀번호와 비밀번호 확인에 입력한 비밀번호가 일치한지 확인한다.
        if(pw.text.toString() == pwCk.text.toString()) {
            initPwCheckSuccess()
            // 키패드 내리기
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(pwCk.windowToken, 0)
            pw.isEnabled = false // 일치 확인 후 변경방치 readonly
            pwCk.isEnabled = false // 일치 확인 후 변경방치 readonly
        } else {
            initPwCheckFail()
        }
    }

    // =======================================회원가입 완료 버튼 클릭=======================================
    private fun initBtnSignUp() {
        /*회원가입 완료 버튼 클릭
          1. 중복확인의 구분값과 비밀번호 일치확인 구분값이 true라면 회원가입이 완료 가능
          2. 중복확인의 구분값이 false라면 중복확인이 되지 않은것!
          3. 비밀번호 일치확인 구분값이 false라면 비밀번호가 일치한지 확인받지 않은 것!*/
        signUpFinishBtn.setOnClickListener {
            if(isEmailSuccess == 0) {
                initSignUpEmailFail()
            } else if(isPwSuccess == 0) {
                initSignUpPwFail()
            } else { // 계정인증과  user DB에 데이터를 추가
                initFbLoginSignUp() // 파이어베이스 계정인증에 회원가입 사용자 등록
            }
        }
    }

    // 파이어베이스 계정인증 로그인정보 등록
    private fun initFbLoginSignUp() {
        val emailClear = email.text.toString()
        val pwClear = pw.text.toString()

        auth.createUserWithEmailAndPassword(emailClear, pwClear).addOnCompleteListener(this) {
                task ->
            if(task.isSuccessful) {
                // 신규계정 생성
                Toast.makeText(this, "회원가입이 완료되었습니다. 로그인 해주세요.", Toast.LENGTH_SHORT).show()
                initFbDBUserInfoInsert() // 파이어베이스 DB user컬렉션에 사용자 정보 등록
                moveLogin(task.result?.user) // 로그인 페이지로 이동한다.
            } else {
                // 이미 계정이 존재한다면?
                Toast.makeText(this, "회원가입에 실패했습니다. 이메일과 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Firebase DB에 데이터 등록하기
    private fun initFbDBUserInfoInsert() {
        val authUser = auth.currentUser?.email

        var userInfo = UserModel()

        userInfo.userId = authUser // 로그인 후 현재 유저의 이메일
        userInfo.uid = auth.uid // uid값
        userInfo.timestamp = System.currentTimeMillis()

        firestore.collection("user").document(authUser!!).set(userInfo)
    }

    // =======================================회원가입 완료=======================================
    /* 회원가입 성공 시 Login페이지 이동 */
    private fun moveLogin(user: FirebaseUser?) {
        if(user != null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // ============================================================Dialog============================================================

    /*<<<<<<<<<< 이메일 >>>>>>>>>>*/
    // =======================================이메일 입력창이 비었다면 Dialog창=======================================
    private fun initEmailEmpty() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("이메일 에러")
            .setIcon(R.drawable.ic_cancel)
            .setMessage("이메일 입력 후 시도해주세요.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    email.requestFocus()
                    isEmailSuccess = 0
                })
            .setCancelable(false)
        builder.show()
    }

    // =======================================이메일 패턴 틀림 Dialog창=======================================
    private fun initPatternErrorEmail() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("이메일 에러")
            .setIcon(R.drawable.ic_info)
            .setMessage("이메일 형식이 아닙니다. 이메일 형식에 맞게 작성해주세요")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    email.text.clear()
                    email.requestFocus()
                    isEmailSuccess = 0
                })
            .setCancelable(false)
        builder.show()
    }

    // =======================================이메일 중복이라면 Dialog창=======================================
    private fun initUnavailableEmail() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("이메일 중복 확인")
            .setIcon(R.drawable.ic_info)
            .setMessage("현재 사용중인 이메일입니다. 다시 이메일을 입력해주세요")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    email.text.clear()
                    isEmailSuccess = 0
                })
            .setCancelable(false)
        builder.show()
    }

    // =======================================이메일 중복 아니라면 사용 가능 Dialog창=======================================
    private fun initAvailableEmail() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("이메일 중복 확인")
            .setIcon(R.drawable.ic_check_circle)
            .setMessage("사용 가능한 이메일입니다.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    pw.requestFocus()
                    isEmailSuccess = 1
                })
            .setCancelable(false)
        builder.show()
    }

    /*<<<<<<<<<< 비밀번호 >>>>>>>>>>*/
    // =======================================비밀번호창이 비었다면 Dialog창=======================================
    private fun initPwEmpty() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("비밀번호 에러")
            .setIcon(R.drawable.ic_cancel)
            .setMessage("비밀번호가 비었습니다. 비밀번호를 입력해주세요")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    pw.requestFocus()
                    isPwSuccess = 0
                })
            .setCancelable(false)
        builder.show()
    }

    // =======================================비밀번호 확인 입력창이 비었다면 Dialog창=======================================
    private fun initPwCkEmpty() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("비밀번호 에러")
            .setIcon(R.drawable.ic_cancel)
            .setMessage("비밀번호가 비었습니다. 비밀번호를 입력해주세요")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    pwCk.requestFocus()
                    isPwSuccess = 0
                })
            .setCancelable(false)
        builder.show()
    }

    // =======================================비밀번호 패턴 틀림 Dialog창=======================================
    private fun initPatternErrorPw() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("비밀번호 에러")
            .setIcon(R.drawable.ic_info)
            .setMessage("비밀번호 패턴이 틀렸습니다. 영문,특수문자포함 8~20자 사이로 입력해주세요")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    pw.text.clear()
                    pw.requestFocus()
                    isPwSuccess = 0
                })
            .setCancelable(false)
        builder.show()
    }

    // =======================================비밀번호 확인 패턴 틀림 Dialog창=======================================
    private fun initPatternErrorPwCk() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("비밀번호 형식 에러")
            .setIcon(R.drawable.ic_info)
            .setMessage("비밀번호 형식이 틀렸습니다. 영문,특수문자포함 8~20자 사이로 입력해주세요")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    pwCk.text.clear()
                    pwCk.requestFocus()
                    isPwSuccess = 0
                })
            .setCancelable(false)
        builder.show()
    }

    // =======================================비밀번호 확인 비밀번호가 일치한다면 Dialog창=======================================
    private fun initPwCheckSuccess() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("비밀번호 확인")
            .setIcon(R.drawable.ic_check_circle)
            .setMessage("비밀번호가 일치합니다. 회원가입을 완료해주세요.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    isPwSuccess = 1
                })
            .setCancelable(false)
        builder.show()
    }

    // =======================================비밀번호 확인 비밀번호가 일치하지 않다면 Dialog창=======================================
    private fun initPwCheckFail() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("비밀번호 확인")
            .setIcon(R.drawable.ic_cancel)
            .setMessage("비밀번호가 불일치합니다. 비밀번호를 다시 확인해주세요.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    pwCk.requestFocus()
                    isPwSuccess = 0
                })
            .setCancelable(false)
        builder.show()
    }

    /*<<<<<<<<<< 회원가입 완료 >>>>>>>>>>*/
    // =======================================회원가입 이메일 중복확인 안됨 Dialog창=======================================
    private fun initSignUpEmailFail() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("회원가입 실패")
            .setIcon(R.drawable.ic_info)
            .setMessage("이메일 중복확인은 필수입니다. 이메일 중복확인 후 다시 시도해주세요")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    email.requestFocus()
                    isEmailSuccess = 0
                })
            .setCancelable(false)
        builder.show()
    }

    // =======================================회원가입 비밀번호 일치확인 안됨 Dialog창=======================================
    private fun initSignUpPwFail() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("회원가입 실패")
            .setIcon(R.drawable.ic_info)
            .setMessage("비밀번호가 일치한지 확인은 필수입니다. 비밀번호 일치 확인 후 다시 시도해주세요")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    isPwSuccess = 0
                })
            .setCancelable(false)
        builder.show()
    }
}