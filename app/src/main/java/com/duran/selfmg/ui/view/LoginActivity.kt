package com.duran.selfmg.ui.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.duran.selfmg.R
import com.duran.selfmg.data.model.UserModel
import com.duran.selfmg.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding // 데이터바인딩 사용
    lateinit var auth: FirebaseAuth // 계정인증
    lateinit var firestore: FirebaseFirestore // Firebase DB
    lateinit var googleSignInClient: GoogleSignInClient // 구글 로그인

    private val email by lazy { binding.edittextId } // 이메일 입력
    private val pw by lazy { binding.edittextPassword } // 비밀번호 입력
    private val loginBtn by lazy { binding.btnEmailLogin } // 일반 로그인
    private val googleLoginBtn by lazy { binding.googleLoginButton } // 구글 로그인 버튼
    private val signUpBtn by lazy { binding.tvSignUp } // 회원가입입

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        // auth를 사용하기 전에 싱글톤패턴으로 인스턴스 받아온다.
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        initSignUpBtnClicked() // 회원가입
        initLoginBtn() // 일반 로그인

    }

    // =======================================회원가입=======================================
    // 로그인 버튼 클릭
    private fun initLoginBtn() {
        /* 일반 로그인
           1. 우선 이메일이 비었는지
           2. 비밀번호가 비었는지
           3. 비어있지 않다면 로그인 시도
           4. 로그인 정보가 없다면 회원가입 알람
           5. DB에 유저가 닉네임이 있는 유저인지 판별
           6. 닉네임이 있다면 컨텐츠가 있는 페이지, 없다면 닉네임 생성 페이지 이동*/
        loginBtn.setOnClickListener {
            if(email.text.isEmpty()) {
                initEmailEmpty()
            } else if (pw.text.isEmpty()) {
                initPwEmpty()
            } else {
                initSignIn()
            }
        }
    }

    // 로그인
    private fun initSignIn() {
        auth.signInWithEmailAndPassword(email.text.toString(), pw.text.toString()).addOnCompleteListener {
                task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                initNicknameSelect()
            } else {
                initLoginFail()
            }
        }
    }

    // 로그인시 닉네임 존재 여부
    private fun initNicknameSelect() {
        val loginEmail = email.text.toString()
        firestore.collection("user").whereEqualTo("userId", loginEmail).get().addOnSuccessListener {
            result ->
            for(item in result.documentChanges) {
                Log.e("tag", "닉네임이 있는 사용자")
                initLoginMoveMain()
            }
            if(result.size() == 0) {
                Log.e("tag", "닉네임이 없습니다.")
                initCreateNickName()
            }
        }

    }

    // 닉네임이 있는 사용자 메인으로 이동
    private fun initLoginMoveMain() {
        // 로그인
        auth.signInWithEmailAndPassword(email.text.toString(), pw.text.toString()).addOnCompleteListener {
            task ->
            if(task.isSuccessful) {
                moveMain(task.result?.user)
            }
        }
    }

    // 닉네임이 없는 사용자는 닉네임만들기 페이지 이동
    private fun initCreateNickName() {
        // 로그인
        auth.signInWithEmailAndPassword(email.text.toString(), pw.text.toString()).addOnCompleteListener {
                task ->
            if(task.isSuccessful) {
                moveCreateNickname(task.result?.user)
            }
        }
    }

    // =======================================닉네임 있는 사용자 로그인=======================================
    private fun moveMain(user: FirebaseUser?) {
        if(user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // =======================================닉네임 없는 사용자 로그인=======================================
    private fun moveCreateNickname(user: FirebaseUser?) {
        if(user != null) {
            val intent = Intent(this, CreateNameActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // =======================================회원가입=======================================
    private fun initSignUpBtnClicked() { // 회원가입 버튼 클릭 -> 회원가입 페이지 이동
        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    // ============================================================Dialog============================================================
    /*<<<<<<<<<< 로그인 >>>>>>>>>>*/
    // =======================================로그인 이메일 입력창이 비었다면 Dialog창=======================================
    private fun initEmailEmpty() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("로그인 에러")
            .setMessage("이메일 입력 후 시도해주세요.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    email.requestFocus()
                })
            .setCancelable(false)
        builder.show()
    }

    // =======================================로그인 비밀번호 입력창이 비었다면 Dialog창=======================================
    private fun initPwEmpty() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("로그인 에러")
            .setMessage("비밀번호 입력 후 시도해주세요.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->
                    pw.requestFocus()
                })
            .setCancelable(false)
        builder.show()
    }

    // =======================================로그인 로그인에 실패했다면 Dialog창=======================================
    private fun initLoginFail() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("로그인 실패")
            .setMessage("존재하지 않는 계정이거나 이메일 또는 비밀번호가 틀렸습니다.")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener { dialog, which ->

                })
            .setCancelable(false)
        builder.show()
    }

}