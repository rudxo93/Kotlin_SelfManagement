package com.duran.selfmg.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.duran.selfmg.R
import com.duran.selfmg.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    // 데이터바인딩 사용
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        /*// 일반 로그인
        binding.btnEmailLogin.setOnClickListener {
            initSignIn()
        }*/
        // 회원가입
        initSignUpBtnClicked()
    }

    // =======================================회원가입 -> 번호인증 이동=======================================
    /* 회원가입 버튼 클릭 -> 회원가입 페이지 이동 */
    private fun initSignUpBtnClicked() {
        binding.tvSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    /* 회원가입 버튼 클릭 -> 회원가입 페이지 이동 */
    // =======================================회원가입 -> 번호인증 이동=======================================

    // =======================================일반 로그인=======================================
    /*private fun initSignIn() {
        val email = binding.edittextId.text.toString()
        val pw = binding.edittextPassword.text.toString()

        auth.signInWithEmailAndPassword(email, pw).addOnCompleteListener {
                task ->
            if (task.isSuccessful) {
                moveMain(task.result?.user)
                Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
            }
        }
    }*/
    // =======================================일반 로그인=======================================

    // =======================================로그인 성공=======================================
    /* 로그인 성공했다면 Main Activity로 이동 */
    private fun moveMain(user: FirebaseUser?) {
        if(user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
    /* 로그인 성공했다면 Main Activity로 이동 */
    // =======================================로그인 성공=======================================
}